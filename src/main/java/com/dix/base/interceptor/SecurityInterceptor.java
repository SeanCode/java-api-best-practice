package com.dix.base.interceptor;

import com.dix.base.common.CoreConfig;
import com.dix.base.common.Const;
import com.dix.base.common.Util;
import com.dix.base.exception.InvalidConfigurationException;
import com.dix.base.exception.ParamNotSetException;
import com.dix.base.exception.TokenInvalidException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by dd on 9/8/15.
 */
@Component
public class SecurityInterceptor implements HandlerInterceptor
{
    private static Logger logger = org.slf4j.LoggerFactory.getLogger(SecurityInterceptor.class);

    private final UserAuth userAuth;

    @Autowired
    public SecurityInterceptor(UserAuth userAuth) {
        this.userAuth = userAuth;

        if (this.userAuth == null)
        {
            throw new InvalidConfigurationException("UserAuth not found");
        }
    }

    private boolean canGuestAccess(String path)
    {
        List<Pattern> patternList = CoreConfig.getInstance().getGuestCanAccessPathPatternList().get();
        return patternList.stream().anyMatch(p -> p.matcher(path).matches());
    }

    private String getRequiredParam(HttpServletRequest httpServletRequest, String key) throws ParamNotSetException {
        if (!httpServletRequest.getParameterMap().containsKey(key))
        {
            throw new ParamNotSetException(key);
        }

        return httpServletRequest.getParameter(key);
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception
    {
        URI uri = new URI(httpServletRequest.getRequestURI());
        String path = uri.getPath();

        // use default cors
        // httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");

        logger.trace("path: {}", path);
        logger.trace("query: {}", httpServletRequest.getQueryString());
        logger.trace("content-type: {}", httpServletRequest.getContentType());
        if (httpServletRequest.getContentType().equals("application/x-www-form-urlencoded"))
        {
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            try {
                BufferedReader reader = httpServletRequest.getReader();
                while ((line = reader.readLine()) != null)
                {
                    stringBuilder.append(line);
                }

            } catch (Exception e) { /*report an error*/ }
            logger.trace("body: {}", stringBuilder.toString());
        }

        if (!canGuestAccess(path))
        {
            String token = getRequiredParam(httpServletRequest, "token");
            Map user = userAuth.getUserByToken(token);

            if (user == null || !user.containsKey(Const.USER_AUTH_KEY_USER_ID))
            {
                throw new TokenInvalidException();
            }

            Long userId = Util.parseLong(user.get(Const.USER_AUTH_KEY_USER_ID));

            httpServletRequest.setAttribute(Const.USER_AUTH_KEY_USER_ID, userId);
        }

        // httpServletRequest.setAttribute("client", getRequiredParam(httpServletRequest, "client"));
        // httpServletRequest.setAttribute("version", getRequiredParam(httpServletRequest, "version"));

        return true;
    }




    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception
    {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception
    {

    }

    public interface UserAuth
    {
        Map<String, Object> getUserByToken(String token);
    }
}
