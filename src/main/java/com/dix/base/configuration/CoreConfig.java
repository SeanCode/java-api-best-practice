package com.dix.base.configuration;

import com.dix.base.common.Util;
import com.dix.base.core.Core;
import com.dix.base.redis.SpringMybatisObjectFactory;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.EnumSet;
import java.util.Set;

@Configuration
public class CoreConfig {

    private final ApplicationContext context;

    private final DataSource dataSource;

    @Autowired
    public CoreConfig(ApplicationContext context, DataSource dataSource) {
        this.context = context;
        this.dataSource = dataSource;
    }

    @PostConstruct
    private void init()
    {
        Core.setContext(context);
        Core.setDataSource(this.dataSource);
        SpringMybatisObjectFactory.setContext(context);

        Util.init();
    }
}