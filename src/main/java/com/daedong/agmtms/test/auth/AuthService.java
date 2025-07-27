package com.daedong.agmtms.test.auth;

import com.daedong.agmtms.test.auth.dto.TokenResponse;
import com.daedong.agmtms.test.config.TestConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private static final OkHttpClient client = new OkHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();
    private final String authUrl;

    public AuthService(String env) {
        Map<String, Object> envConfig = TestConfig.getEnvConfig(env);
        this.authUrl = envConfig.get("auth-url") + "/getToken";
    }

    public TokenResponse login(String userId, String password) {
        try {
            Map<String, String> loginRequest = new HashMap<>();
            loginRequest.put("userId", userId);
            loginRequest.put("password", password);
            String json = mapper.writeValueAsString(loginRequest);

            RequestBody body = RequestBody.create(
                    json, MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url(authUrl)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "*/*")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("User-Agent", "OkHttp-ApiTest/1.0")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    logger.error("로그인 실패: HTTP {} - {}", response.code(), response.message());
                    throw new RuntimeException("로그인 실패: " + response.code());
                }
                String responseBody = response.body().string();
                TokenResponse token = mapper.readValue(responseBody, TokenResponse.class);
                logger.info("토큰 발급 성공: {}", token.getAccessToken());
                return token;
            }
        } catch (Exception e) {
            logger.error("인증 오류", e);
            throw new RuntimeException(e);
        }
    }
}
