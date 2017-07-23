package com.dix.app.controller;

import com.dix.app.model.Token;
import com.dix.app.model.User;
import com.dix.base.common.*;
import com.dix.app.service.UserService;
import com.dix.base.core.CoreQuery;
import com.dix.base.exception.BaseException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.tomcat.jdbc.pool.DataSource;

import static org.jooq.impl.DSL.*;

import org.jooq.*;
import org.jooq.impl.DSL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    private static Logger logger = LoggerFactory.getLogger(TestController.class);

    private final RedisTemplate<String, String> template;
    private final UserService userService;
    private final Configuration configuration;
    private final Redis redis;

    @Autowired
    public TestController(@Qualifier("RedisTemplate") RedisTemplate<String, String> template, UserService userService, Configuration configuration, Redis redis) {
        this.template = template;
        this.userService = userService;
        this.configuration = configuration;
        this.redis = redis;
    }

    @RequestMapping("/transaction")
    public DataResponse transaction() {
        // userService.testTransaction();
        return DataResponse.create();
    }

    @RequestMapping("/template")
    public DataResponse template() throws IOException, TemplateException {
        Template template = configuration.getTemplate("hello.tpl");
        User user = userService.findById(1L);
        Map<String, Object> data = new HashMap<>();
        data.put("user", user);
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, data);
        return DataResponse.create()
                .put("content", content)
                ;
    }

    @RequestMapping("/email")
    public DataResponse email() throws IOException, TemplateException, EmailException {
        Template template = configuration.getTemplate("hello.tpl");
        User user = userService.findById(1L);
        Map<String, Object> data = new HashMap<>();
        data.put("user", user);
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, data);

        Email email = new SimpleEmail();
        email.setHostName("smtp-mail.outlook.com");
        // email.setSslSmtpPort("587");
        email.setSmtpPort(587);
        email.setSocketTimeout(60 * 1000);
        email.setSocketConnectionTimeout(10 * 1000);
        email.setAuthentication("xxx", "xxx");
        // email.setSSLOnConnect(false);
        email.setStartTLSEnabled(true);
        // email.setStartTLSRequired(true);
        email.setFrom("xxx");
        email.setSubject("hello");
        email.setMsg(content);
        email.addTo("xxx");
        email.send();

        return DataResponse.create()
                ;
    }

    @RequestMapping("/email-qq")
    public DataResponse emailQQ() throws IOException, TemplateException, EmailException {
        Template template = configuration.getTemplate("hello.tpl");
        User user = userService.findById(1L);
        Map<String, Object> data = new HashMap<>();
        data.put("user", user);
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, data);

        Email email = new SimpleEmail();
        email.setHostName("smtp.qq.com");
        email.setSslSmtpPort("465");
        email.setSmtpPort(465);
        email.setSocketTimeout(30 * 1000);
        email.setSocketConnectionTimeout(10 * 1000);
        email.setAuthentication("xxx", "xxx");
        email.setSSLOnConnect(true);
        email.setStartTLSEnabled(true);
        // email.setStartTLSRequired(true);
        email.setFrom("xxx");
        email.setSubject("hello");
        email.setMsg(content);
        email.addTo("xxx");
        email.send();

        return DataResponse.create()
                ;
    }

    @RequestMapping("/redis-get")
    public DataResponse redisGet() {
        Long time = Util.time();
        Long cacheTime = redis.get("dd", () -> time, Long.class, 3);

        return DataResponse.create()
                .put("time", cacheTime)
                ;
    }

    @RequestMapping("/redis-lock")
    public DataResponse redisLock() {

        int port = makePort();

        return DataResponse.create()
                .put("port", port)
                ;
    }

    public int makePort()
    {
        String lockKey = "lock-test";
        int lockCount = 0;
        while (!redis.lock(lockKey, 10))
        {
            lockCount++;
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (lockCount > 5)
            {
                throw new BaseException(20002, "busy, try later");
            }
        }


        int port = -1;
        try (Jedis jedis = redis.getClient()) {
            String keyOfPortBit = "port.bit";
            Long offset = jedis.bitpos(keyOfPortBit, false);
            if (offset < 0 || offset >= 5000) {
                throw new BaseException(20001, "port not available");
            }

            jedis.setbit(keyOfPortBit, offset, true);

            String keyOfPortSet = "port.set";
            jedis.zincrby(keyOfPortSet, offset, offset.toString());

            port = offset.intValue();
        } finally {
            redis.unlock(lockKey);
        }

        return port;
    }

    @RequestMapping("/exception")
    public DataResponse exception() {
        throw new BaseException(0, "test");
    }

    @Autowired
    DataSource dataSource;

    @RequestMapping("/jooq")
    public DataResponse jooq() {


//        DSLContext dslContext = DSL.using(this.dataSource, SQLDialect.MYSQL);
//        Result<Record> result = dslContext.select().from("user")
//                .where(field("id").eq("1"))
//                .fetch();
//
//        logger.info("result {}", result);
//        Map<String, Object> user = Core.processModel(result.intoMaps().get(0), User.class);
//        logger.info("map {}", user);


        logger.info("{}", Core.Q().findById(User.class, 1L));
        logger.info("{}", Core.Q().findByCol(User.class, "id", 1L));

        SelectQuery<Record> qb = Core.Q().createQuery(User.class);
        qb.addConditions(field("id").eq(3L));
        logger.info("{}", Core.Q().executeQuery(qb));

        return DataResponse.create()
                .put("user", "")
                ;
    }

    @RequestMapping("/save")
    public DataResponse save() {

        Token token = new Token();
        token.setToken(Token.makeToken());
        token.setUserId(1L);
        token.setStatus(Token.STATUS_VALID);

        Long time = Util.time();
        token.setExpireTime(0L);
        token.setCreateTime(time);
        token.setUpdateTime(time);

        token.save();

        return DataResponse.create()
                .put("token", token)
                ;
    }


}
