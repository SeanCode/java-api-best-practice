package com.dix.configuration;

import com.dix.common.Core;
import com.dix.common.SpringMybatisObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class ContextConfig {

    @Autowired
    ApplicationContext context;

    @PostConstruct
    private void init()
    {
        Core.setContext(context);
        SpringMybatisObjectFactory.setContext(context);
    }
}