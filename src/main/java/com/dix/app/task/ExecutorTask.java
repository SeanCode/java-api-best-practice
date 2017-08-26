package com.dix.app.task;

import com.dix.base.core.CoreTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ExecutorTask {

    private static final Logger logger = LoggerFactory.getLogger(ExecutorTask.class);

    @Scheduled(fixedRate = 100)
    public void execute() {
        CoreTask.loop();
    }

}