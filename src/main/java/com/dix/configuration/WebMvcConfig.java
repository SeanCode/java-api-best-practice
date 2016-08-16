package com.dix.configuration;

import com.dix.interceptor.SecurityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

  @Autowired
  SecurityInterceptor securityInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
      registry.addInterceptor(securityInterceptor).addPathPatterns("/**");
  }
}