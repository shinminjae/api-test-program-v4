package com.daedong.agmtms.test;

import com.daedong.agmtms.test.auth.AuthService;
import com.daedong.agmtms.test.api.AdcuApiTestService;
import com.daedong.agmtms.test.api.BaseApiTestService;
import com.daedong.agmtms.test.api.ManagementApiTestService;
import com.daedong.agmtms.test.api.MobileApiTestService;
import com.daedong.agmtms.test.api.PathParamExampleService;
import com.daedong.agmtms.test.api.dto.TestResult;
import com.daedong.agmtms.test.report.TestReporter;
import com.daedong.agmtms.test.config.TestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainApplication {
    private static final Logger logger = LoggerFactory.getLogger(MainApplication.class);

    public static void main(String[] args) {
        String env = "tms-dev"; // 기본값
        String apiType = "adcu"; // 기본값: 모든 API 테스트

        // 명령행 인수 파싱
        for (String arg : args) {
            if (arg.startsWith("--env=")) {
                env = arg.split("=")[1];
            } else if (arg.startsWith("--api=")) {
                apiType = arg.split("=")[1];
            }
        }

        if ("tms-dev".equals(env)) {
            logger.info("API 테스트 프로그램 시작 - 환경: {}, API 타입: {}", env, apiType);
            Map<String, Object> envConfig = TestConfig.getEnvConfig(env);

            String userId = (String) ((Map<String, Object>) envConfig.get("credentials")).get("userId");
            String password = (String) ((Map<String, Object>) envConfig.get("credentials")).get("password");

            AuthService authService = new AuthService(env);
            String accessToken = authService.login(userId, password).getAccessToken();

            List<BaseApiTestService> testServices = new ArrayList<>();

            // API 타입에 따른 테스트 서비스 선택
            switch (apiType.toLowerCase()) {
                case "adcu":
                    logger.info("ADCU API 테스트 실행");
                    AdcuApiTestService adcuService = new AdcuApiTestService(env, accessToken);

                    adcuService.testTmsAdcu();
                    adcuService.analyzeResponses();
                    testServices.add(adcuService);

                    break;

                case "all":
                default:
                    logger.info("모든 API 테스트 실행");
                    
                    // ADCU API 테스트
                    logger.info("=== ADCU API 테스트 시작 ===");
                    AdcuApiTestService adcuApiTestService = new AdcuApiTestService(env, accessToken);
                    adcuApiTestService.testTmsAdcu();
                    adcuApiTestService.analyzeResponses();
                    testServices.add(adcuApiTestService);

            }

            // 통합 테스트 결과 생성
            if (!testServices.isEmpty()) {
                TestReporter reporter = new TestReporter();
                
                // 모든 테스트 결과 수집
                List<TestResult> allResults = new ArrayList<>();
                for (BaseApiTestService service : testServices) {
                    allResults.addAll(service.getResults());
                }
                
                // HTML 리포트 생성
                reporter.generateHtmlReport(allResults);
                
                // 테스트 결과 요약 출력
                printTestSummary(allResults);
            }

            logger.info("테스트 완료");
        } else {
            logger.error("지원하지 않는 환경: {}", env);
        }
    }

    // 테스트 결과 요약 출력
    private static void printTestSummary(List<TestResult> results) {
        logger.info("=== 테스트 결과 요약 ===");
        
        int totalTests = results.size();
        int successCount = (int) results.stream().filter(TestResult::isSuccess).count();
        int failureCount = totalTests - successCount;
        
        long totalResponseTime = results.stream()
                .mapToLong(TestResult::getResponseTime)
                .sum();
        double avgResponseTime = totalTests > 0 ? (double) totalResponseTime / totalTests : 0;
        
        logger.info("총 테스트 수: {}", totalTests);
        logger.info("성공: {}", successCount);
        logger.info("실패: {}", failureCount);
        logger.info("성공률: {:.2f}%", (double) successCount / totalTests * 100);
        logger.info("평균 응답 시간: {:.2f}ms", avgResponseTime);
        
        // 실패한 테스트들 출력
        if (failureCount > 0) {
            logger.info("=== 실패한 테스트 목록 ===");
            results.stream()
                    .filter(result -> !result.isSuccess())
                    .forEach(result -> {
                        logger.error("실패: {} - 상태코드: {}, 응답시간: {}ms, 재시도: {}회, 오류: {}", 
                                result.getApi(), 
                                result.getStatusCode(), 
                                result.getResponseTime(), 
                                result.getRetryCount(),
                                result.getErrorMessage());
                    });
        }
        
        logger.info("=== 테스트 결과 요약 완료 ===");
    }
}
