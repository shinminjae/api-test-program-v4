package com.daedong.agmtms.test.api;

import com.daedong.agmtms.test.api.dto.ApiRequest;
import com.daedong.agmtms.test.api.dto.TestResult;
import com.daedong.agmtms.test.config.TestConfig;
import com.daedong.agmtms.test.monitor.TestMonitor;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

public class ApiTestService {
    private static final Logger logger = LoggerFactory.getLogger(ApiTestService.class);
    private final String env;
    private final String token;
    private final List<TestResult> results = new ArrayList<>();
    private final TestMonitor monitor = new TestMonitor();
    private final OkHttpClient client = new OkHttpClient();

    private final int retryCount;
    private final int concurrentRequests;
    private final String apiUrl;

    public ApiTestService(String env, String token) {
        this.env = env;
        this.token = token;

        Map<String, Object> testCfg = TestConfig.getTestConfig();
        this.retryCount = (int) testCfg.get("retry-count");
        this.concurrentRequests = (int) testCfg.get("concurrent-requests");

        Map<String, Object> envConfig = TestConfig.getEnvConfig(env);
        this.apiUrl = (String) envConfig.get("api-url");
    }

    public void testAllApis() {
        List<ApiRequest> apis = Arrays.asList(
                new ApiRequest("/vehicle/status/list", "GET", null, null),
                new ApiRequest("/customer/list", "GET", null, null),
                new ApiRequest("/statistics/vehicle", "GET", null, null),
                new ApiRequest("/report/daily", "GET", null, null)
        );

        ExecutorService executor = Executors.newFixedThreadPool(concurrentRequests);
        List<Future<?>> futures = new ArrayList<>();

        for (ApiRequest api : apis) {
            futures.add(executor.submit(() -> testApiWithRetry(api)));
        }

        for (Future<?> f : futures) {
            try {
                f.get();
            } catch (Exception e) {
                logger.error("테스트 실행 오류", e);
            }
        }

        executor.shutdown();
    }

    private void testApiWithRetry(ApiRequest apiRequest) {
        int attempts = 0;
        boolean success = false;
        int statusCode = 0;
        long responseTime = 0;
        String message = "";

        while (attempts < retryCount && !success) {
            attempts++;
            try {
                long start = System.currentTimeMillis();
                Request.Builder builder = new Request.Builder()
                        .url(apiUrl + apiRequest.getEndpoint())
                        .addHeader("X-AUTH-ACCESS-TOKEN", token)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Accept", "application/json");

                if (apiRequest.getHeaders() != null) {
                    apiRequest.getHeaders().forEach(builder::addHeader);
                }

                if ("POST".equalsIgnoreCase(apiRequest.getMethod()) ||
                    "PUT".equalsIgnoreCase(apiRequest.getMethod())) {
                    RequestBody body = RequestBody.create(
                            apiRequest.getBody() != null ? apiRequest.getBody() : "{}",
                            MediaType.parse("application/json")
                    );
                    if ("POST".equalsIgnoreCase(apiRequest.getMethod()))
                        builder.post(body);
                    else
                        builder.put(body);
                } else {
                    builder.get();
                }

                try (Response response = client.newCall(builder.build()).execute()) {
                    responseTime = System.currentTimeMillis() - start;
                    statusCode = response.code();
                    success = response.isSuccessful();
                    message = response.body() != null ? response.body().string() : "";

                    monitor.logProgress(apiRequest.getEndpoint(), success, responseTime);

                    if (success) {
                        logger.info("[SUCCESS] {} ({}ms): {}", apiRequest.getEndpoint(), responseTime, message);
                    } else {
                        logger.error("[FAIL] {} ({}ms): HTTP {} - {}", apiRequest.getEndpoint(), responseTime, statusCode, message);
                    }
                }
            } catch (Exception e) {
                logger.error("API 호출 실패: {}", apiRequest.getEndpoint(), e);
            }
        }
        results.add(new TestResult(apiRequest.getMethod() + " " + apiRequest.getEndpoint(),
                success, statusCode, responseTime));
    }

    public List<TestResult> getResults() {
        return results;
    }
}
