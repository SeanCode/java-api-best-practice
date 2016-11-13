package com.dix.app.controller;

import com.dix.app.model.User;
import com.dix.base.common.DataResponse;
import com.dix.app.service.UserService;
import com.dix.base.common.Redis;
import com.dix.base.common.Util;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

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



}
