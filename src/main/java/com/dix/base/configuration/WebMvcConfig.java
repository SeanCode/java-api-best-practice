package com.dix.base.configuration;

import com.dix.base.common.CoreConfig;
import com.dix.base.interceptor.SecurityInterceptor;
import com.dix.base.service.UploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    private static Logger logger = LoggerFactory.getLogger(WebMvcConfig.class);

    private final SecurityInterceptor securityInterceptor;

    @Autowired
    public WebMvcConfig(SecurityInterceptor securityInterceptor) {
        this.securityInterceptor = securityInterceptor;
      }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        List<String> excludePathPatternList = CoreConfig.getInstance().getDefaultInterceptorExcludePathList().get();

        registry.addInterceptor(securityInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(excludePathPatternList.toArray(new String[excludePathPatternList.size()]))
        ;
    }

    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }

}