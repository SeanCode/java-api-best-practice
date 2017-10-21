package com.dix.app.task.executor;

import com.dix.base.common.Util;
import com.dix.base.core.TaskExecutorInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

// for CoreTask
public class TestExecutor {
    private static final Logger logger = LoggerFactory.getLogger(TestExecutor.class);

    public static void execute(Map<String, Object> data) {
        logger.info("run: {}", Util.jsonEncode(data));
    }
}
