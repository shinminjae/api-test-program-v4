package com.daedong.agmtms.test.api;

import com.daedong.agmtms.test.api.dto.ApiRequest;
import com.daedong.agmtms.test.api.dto.ApiResponse;
import com.daedong.agmtms.test.api.dto.TestResult;
import com.daedong.agmtms.test.config.TestConfig;
import com.daedong.agmtms.test.monitor.TestMonitor;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public abstract class BaseApiTestService {
    private static final Logger logger = LoggerFactory.getLogger(BaseApiTestService.class);
    
    protected final String env;
    protected final String token;
    protected final List<TestResult> results = new ArrayList<>();
    protected final TestMonitor monitor = new TestMonitor();
    protected final OkHttpClient client = new OkHttpClient();
    
    protected final int retryCount;
    protected final int concurrentRequests;
    protected final String apiUrl;

    public BaseApiTestService(String env, String token) {
        this.env = env;
        this.token = token;

        Map<String, Object> testCfg = TestConfig.getTestConfig();
        this.retryCount = (int) testCfg.get("retry-count");
        this.concurrentRequests = (int) testCfg.get("concurrent-requests");

        Map<String, Object> envConfig = TestConfig.getEnvConfig(env);
        this.apiUrl = (String) envConfig.get("api-url");
    }

    // 기본 헤더 설정
    protected ApiRequest createDefaultHeaders(ApiRequest request) {
        return request
                .addHeader("X-AUTH-ACCESS-TOKEN", token)
                .addHeader("Content-Type", request.getContentType())
                .addHeader("Accept", "application/json")
                .addHeader("User-Agent", "API-Test-Client/1.0");
    }

    // API 호출 실행
    protected ApiResponse executeApiCall(ApiRequest apiRequest) {
        long startTime = System.currentTimeMillis();
        
        try {
            // Path 파라미터 검증
            if (apiRequest.hasPathParams()) {
                if (!apiRequest.validatePathParams()) {
                    System.err.println("Path 파라미터 검증 실패로 API 호출을 중단합니다.");
                    return new ApiResponse(400, "Path parameter validation failed", 
                            System.currentTimeMillis() - startTime);
                }
            }
            
            Request.Builder builder = new Request.Builder()
                    .url(apiRequest.getFullUrl(apiUrl));

            // 헤더 설정
            apiRequest.getHeaders().forEach(builder::addHeader);

            // 요청 바디 설정
            if (apiRequest.isMultipart()) {
                builder.method(apiRequest.getMethod(), createMultipartBody(apiRequest));
            } else if ("POST".equalsIgnoreCase(apiRequest.getMethod()) || 
                       "PUT".equalsIgnoreCase(apiRequest.getMethod()) ||
                       "PATCH".equalsIgnoreCase(apiRequest.getMethod())) {
                RequestBody body = RequestBody.create(
                        apiRequest.getBody() != null ? apiRequest.getBody() : "{}",
                        MediaType.parse(apiRequest.getContentType())
                );
                
                switch (apiRequest.getMethod().toUpperCase()) {
                    case "POST":
                        builder.post(body);
                        break;
                    case "PUT":
                        builder.put(body);
                        break;
                    case "PATCH":
                        builder.patch(body);
                        break;
                }
            } else {
                builder.method(apiRequest.getMethod(), null);
            }

            try (Response response = client.newCall(builder.build()).execute()) {
                long responseTime = System.currentTimeMillis() - startTime;
                String responseBody = response.body() != null ? response.body().string() : "";
                
                return new ApiResponse(response.code(), responseBody, responseTime);
            }
        } catch (IOException e) {
            long responseTime = System.currentTimeMillis() - startTime;
            logger.error("API 호출 실패: {}", apiRequest.getEndpoint(), e);
            return new ApiResponse(0, "IOException: " + e.getMessage(), responseTime);
        }
    }

    // 멀티파트 바디 생성
    private RequestBody createMultipartBody(ApiRequest apiRequest) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        for (Map.Entry<String, Object> entry : apiRequest.getMultipartData().entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            
            if (value instanceof File) {
                File file = (File) value;
                builder.addFormDataPart(key, file.getName(), 
                    RequestBody.create(file, MediaType.parse("application/octet-stream")));
            } else if (value instanceof String) {
                builder.addFormDataPart(key, (String) value);
            } else {
                builder.addFormDataPart(key, value.toString());
            }
        }
        
        return builder.build();
    }

    // API 테스트 실행 (재시도 포함)
    protected TestResult testApiWithRetry(ApiRequest apiRequest) {
        int attempts = 0;
        ApiResponse lastResponse = null;
        String lastError = null;

        while (attempts < retryCount) {
            attempts++;
            try {
                lastResponse = executeApiCall(apiRequest);
                
                if (lastResponse.isSuccess()) {
                    monitor.logProgress(apiRequest.getEndpoint(), true, lastResponse.getResponseTime());
                    logger.info("[SUCCESS] {} ({}ms): {}", 
                            apiRequest.getEndpoint(), 
                            lastResponse.getResponseTime(), 
                            lastResponse.getRawResponse());
                    
                    return new TestResult(
                            apiRequest.getMethod() + " " + apiRequest.getEndpoint(),
                            true,
                            lastResponse.getStatusCode(),
                            lastResponse.getResponseTime(),
                            null,
                            lastResponse,
                            attempts
                    );
                } else {
                    lastError = String.format("HTTP %d: %s", 
                            lastResponse.getStatusCode(), 
                            lastResponse.getRawResponse());
                    logger.warn("[RETRY {}] {} - {}", attempts, apiRequest.getEndpoint(), lastError);
                }
            } catch (Exception e) {
                lastError = e.getMessage();
                logger.error("[RETRY {}] API 호출 실패: {} - {}", attempts, apiRequest.getEndpoint(), lastError);
            }
        }

        // 모든 재시도 실패
        monitor.logProgress(apiRequest.getEndpoint(), false, 
                lastResponse != null ? lastResponse.getResponseTime() : 0);
        logger.error("[FAIL] {} - 최종 실패: {}", apiRequest.getEndpoint(), lastError);
        
        return new TestResult(
                apiRequest.getMethod() + " " + apiRequest.getEndpoint(),
                false,
                lastResponse != null ? lastResponse.getStatusCode() : 0,
                lastResponse != null ? lastResponse.getResponseTime() : 0,
                lastError,
                lastResponse,
                attempts
        );
    }

    // 동시 실행을 위한 메서드
    protected void executeConcurrentTests(List<ApiRequest> apiRequests) {
        ExecutorService executor = Executors.newFixedThreadPool(concurrentRequests);
        List<Future<TestResult>> futures = new ArrayList<>();

        for (ApiRequest api : apiRequests) {
            futures.add(executor.submit(() -> testApiWithRetry(api)));
        }

        for (Future<TestResult> future : futures) {
            try {
                TestResult result = future.get();
                results.add(result);
            } catch (Exception e) {
                logger.error("테스트 실행 오류", e);
            }
        }

        executor.shutdown();
    }

    // 결과 반환
    public List<TestResult> getResults() {
        return new ArrayList<>(results);
    }

    // 추상 메서드 - 각 서비스에서 구현
    public abstract void testTmsAdcu();
} 