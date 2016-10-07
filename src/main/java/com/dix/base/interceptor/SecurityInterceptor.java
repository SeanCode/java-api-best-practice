package com.dix.base.interceptor;

import com.dix.base.common.Config;
import com.dix.base.exception.ParamNotSetException;
import com.dix.base.exception.TokenInvalidException;
import com.dix.app.model.Token;
import com.dix.app.service.TokenService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;

/**
 * Created by dd on 9/8/15.
 */
@Component
public class SecurityInterceptor implements HandlerInterceptor
{
    private static Logger logger = org.slf4j.LoggerFactory.getLogger(SecurityInterceptor.class);

    private final TokenService tokenService;

    @Autowired
    public SecurityInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    private boolean canGuestAccess(String path)
    {
        for (int i = 0; i < Config.PATH_GUEST_CAN_ACCESS_PATTERN.length; i++)
        {
            if (Config.PATH_GUEST_CAN_ACCESS_PATTERN[i].matcher(path).matches())
            {
                return true;
            }
        }

        return false;
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

        logger.trace(String.format("path: %s", path));
        System.out.println(String.format("path: %s", path));

        if (!canGuestAccess(path))
        {
            String token = getRequiredParam(httpServletRequest, "token");
            Token tokenEntity = tokenService.getToken(token);

            if (token == null || tokenEntity == null)
            {
                throw new TokenInvalidException();
            }

            httpServletRequest.setAttribute("userId", tokenEntity.getUserId());
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
}
