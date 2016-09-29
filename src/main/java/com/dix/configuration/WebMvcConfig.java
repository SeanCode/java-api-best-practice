package com.dix.configuration;

import com.dix.interceptor.SecurityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

  private final SecurityInterceptor securityInterceptor;

  @Autowired
  public WebMvcConfig(SecurityInterceptor securityInterceptor) {
    this.securityInterceptor = securityInterceptor;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
      registry.addInterceptor(securityInterceptor)
              .addPathPatterns("/**")
              .excludePathPatterns("/error**")
              .excludePathPatterns("/static/**")
      ;
  }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
        .allowedOrigins("*")
        .allowedMethods("OPTIONS", "HEAD", "POST", "GET");
    }

}