package com.daedong.agmtms.test.api.dto;

public class TestResult {
    private String api;
    private boolean success;
    private int statusCode;
    private long responseTime;

    public TestResult(String api, boolean success, int statusCode, long responseTime) {
        this.api = api;
        this.success = success;
        this.statusCode = statusCode;
        this.responseTime = responseTime;
    }

    public String getApi() { return api; }
    public boolean isSuccess() { return success; }
    public int getStatusCode() { return statusCode; }
    public long getResponseTime() { return responseTime; }
}
