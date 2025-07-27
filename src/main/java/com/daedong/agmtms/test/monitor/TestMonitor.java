package com.daedong.agmtms.test.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestMonitor {
    private static final Logger logger = LoggerFactory.getLogger(TestMonitor.class);

    public void logProgress(String api, boolean success, long responseTime) {
        String status = success ? "성공" : "실패";
        logger.info("[MONITOR] {} - {} ({}ms)", api, status, responseTime);
    }
}
