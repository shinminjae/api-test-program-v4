package com.daedong.agmtms.test;

import com.daedong.agmtms.test.auth.AuthService;
import com.daedong.agmtms.test.api.ApiTestService;
import com.daedong.agmtms.test.report.TestReporter;
import com.daedong.agmtms.test.config.TestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class MainApplication {
    private static final Logger logger = LoggerFactory.getLogger(MainApplication.class);

    public static void main(String[] args) {
        String env = "dev"; // 기본값
        if (args.length > 0 && args[0].startsWith("--env=")) {
            env = args[0].split("=")[1];
        }

        logger.info("API 테스트 프로그램 시작 - 환경: {}", env);
        Map<String, Object> envConfig = TestConfig.getEnvConfig(env);

        String userId = (String) ((Map<String, Object>) envConfig.get("credentials")).get("userId");
        String password = (String) ((Map<String, Object>) envConfig.get("credentials")).get("password");

        AuthService authService = new AuthService(env);
        String accessToken = authService.login(userId, password).getAccessToken();

        ApiTestService apiTestService = new ApiTestService(env, accessToken);
        apiTestService.testAllApis();

        TestReporter reporter = new TestReporter();
        reporter.generateHtmlReport(apiTestService.getResults());
        logger.info("테스트 완료");
    }
}
