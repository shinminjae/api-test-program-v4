package com.daedong.agmtms.test.monitor;

import com.daedong.agmtms.test.api.dto.TestResult;

import java.util.List;

public class PerformanceCollector {
    public long getAverageResponseTime(List<TestResult> results) {
        return (long) results.stream().mapToLong(TestResult::getResponseTime).average().orElse(0);
    }

    public long getMaxResponseTime(List<TestResult> results) {
        return results.stream().mapToLong(TestResult::getResponseTime).max().orElse(0);
    }

    public double getSuccessRate(List<TestResult> results) {
        long successCount = results.stream().filter(TestResult::isSuccess).count();
        return ((double) successCount / results.size()) * 100.0;
    }
}
