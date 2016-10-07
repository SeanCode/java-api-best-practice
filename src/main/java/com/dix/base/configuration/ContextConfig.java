package com.dix.base.configuration;

import com.dix.base.common.Core;
import com.dix.base.common.SpringMybatisObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class ContextConfig {

    private final ApplicationContext context;

    @Autowired
    public ContextConfig(ApplicationContext context) {
        this.context = context;
    }

    @PostConstruct
    private void init()
    {
        Core.setContext(context);
        SpringMybatisObjectFactory.setContext(context);
    }
}