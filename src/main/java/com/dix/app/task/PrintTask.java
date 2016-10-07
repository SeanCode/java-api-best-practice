package com.dix.app.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class PrintTask {

    private static final Logger log = LoggerFactory.getLogger(PrintTask.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 10000000)
    public void reportCurrentTime() {
        log.info("The time is now {}", dateFormat.format(new Date()));
    }

    @Scheduled(fixedDelay = 50000000)
    public void print() {
        try {
            Thread.sleep(2000);
        }
        catch (Exception e)
        {

        }
        log.info("hello {}", dateFormat.format(new Date()));
    }
}