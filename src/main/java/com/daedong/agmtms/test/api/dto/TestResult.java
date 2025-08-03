package com.daedong.agmtms.test.api.dto;

import java.time.LocalDateTime;

public class TestResult {
    private String api;
    private boolean success;
    private int statusCode;
    private long responseTime;
    private String errorMessage;
    private ApiResponse apiResponse;
    private LocalDateTime testTime;
    private int retryCount;

    public TestResult(String api, boolean success, int statusCode, long responseTime) {
        this.api = api;
        this.success = success;
        this.statusCode = statusCode;
        this.responseTime = responseTime;
        this.testTime = LocalDateTime.now();
    }

    public TestResult(String api, boolean success, int statusCode, long responseTime, 
                     String errorMessage, ApiResponse apiResponse, int retryCount) {
        this(api, success, statusCode, responseTime);
        this.errorMessage = errorMessage;
        this.apiResponse = apiResponse;
        this.retryCount = retryCount;
    }

    // Getter 메서드들
    public String getApi() { return api; }
    public boolean isSuccess() { return success; }
    public int getStatusCode() { return statusCode; }
    public long getResponseTime() { return responseTime; }
    public String getErrorMessage() { return errorMessage; }
    public ApiResponse getApiResponse() { return apiResponse; }
    public LocalDateTime getTestTime() { return testTime; }
    public int getRetryCount() { return retryCount; }

    // Setter 메서드들
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public void setApiResponse(ApiResponse apiResponse) { this.apiResponse = apiResponse; }
    public void setRetryCount(int retryCount) { this.retryCount = retryCount; }

    @Override
    public String toString() {
        return String.format("TestResult{api='%s', success=%s, statusCode=%d, responseTime=%dms, retryCount=%d, testTime=%s}", 
                api, success, statusCode, responseTime, retryCount, testTime);
    }
}
