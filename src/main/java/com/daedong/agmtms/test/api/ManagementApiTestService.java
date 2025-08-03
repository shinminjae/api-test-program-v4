package com.daedong.agmtms.test.api;

import com.daedong.agmtms.test.api.dto.ApiRequest;
import com.daedong.agmtms.test.api.dto.ApiResponse;
import com.daedong.agmtms.test.api.dto.TestResult;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ManagementApiTestService extends BaseApiTestService {
    private static final Logger logger = LoggerFactory.getLogger(ManagementApiTestService.class);

    public ManagementApiTestService(String env, String token) {
        super(env, token);
    }

    @Override
    public void testTmsAdcu() {
        logger.info("=== 관리 시스템 API 전체 테스트 시작 ===");
        
        // 각 카테고리별 테스트 실행
        testUserManagementApis();
        testSystemConfigApis();
        testNotificationApis();
        testAuditLogApis();
        
        logger.info("=== 관리 시스템 API 전체 테스트 완료 ===");
    }

    // 사용자 관리 API 테스트
    public void testUserManagementApis() {
        logger.info("--- 사용자 관리 API 테스트 시작 ---");
        
        List<ApiRequest> userApis = new ArrayList<>();
        
        // 사용자 목록 조회
        userApis.add(createDefaultHeaders(new ApiRequest("/admin/users", "GET"))
                .addQueryParam("page", "1")
                .addQueryParam("size", "20")
                .addQueryParam("role", "ADMIN"));
        
        // 사용자 상세 정보 조회 (Path 파라미터 사용)
        userApis.add(createDefaultHeaders(new ApiRequest("/admin/users/{userId}", "GET"))
                .addPathParam("userId", "USER_001"));
        
        // 사용자 등록
        userApis.add(createDefaultHeaders(new ApiRequest("/admin/users/register", "POST"))
                .setBody("{\"username\":\"testadmin\",\"email\":\"admin@test.com\",\"role\":\"ADMIN\",\"department\":\"IT\"}"));
        
        // 사용자 정보 수정 (Path 파라미터 사용)
        userApis.add(createDefaultHeaders(new ApiRequest("/admin/users/{userId}", "PUT"))
                .addPathParam("userId", "USER_001")
                .setBody("{\"username\":\"updatedadmin\",\"email\":\"updated@test.com\",\"role\":\"ADMIN\",\"department\":\"IT\"}"));
        
        // 사용자 삭제 (Path 파라미터 사용)
        userApis.add(createDefaultHeaders(new ApiRequest("/admin/users/{userId}", "DELETE"))
                .addPathParam("userId", "USER_002"));
        
        // 사용자 권한 수정 (Path 파라미터 사용)
        userApis.add(createDefaultHeaders(new ApiRequest("/admin/users/{userId}/permissions", "PUT"))
                .addPathParam("userId", "USER_001")
                .setBody("{\"permissions\":[\"READ\",\"WRITE\",\"DELETE\"]}"));
        
        // 사용자 비밀번호 변경 (Path 파라미터 사용)
        userApis.add(createDefaultHeaders(new ApiRequest("/admin/users/{userId}/password", "PUT"))
                .addPathParam("userId", "USER_001")
                .setBody("{\"newPassword\":\"newPassword123\",\"confirmPassword\":\"newPassword123\"}"));
        
        // 사용자 활동 로그 조회 (Path 파라미터 사용)
        userApis.add(createDefaultHeaders(new ApiRequest("/admin/users/{userId}/activity-logs", "GET"))
                .addPathParam("userId", "USER_001")
                .addQueryParam("startDate", "2024-01-01")
                .addQueryParam("endDate", "2024-12-31")
                .addQueryParam("page", "1")
                .addQueryParam("size", "50"));
        
        executeConcurrentTests(userApis);
        logger.info("--- 사용자 관리 API 테스트 완료 ---");
    }

    // 시스템 설정 API 테스트
    public void testSystemConfigApis() {
        logger.info("--- 시스템 설정 API 테스트 시작 ---");
        
        List<ApiRequest> configApis = new ArrayList<>();
        
        // 시스템 설정 조회
        configApis.add(createDefaultHeaders(new ApiRequest("/admin/config/system", "GET")));
        
        // 환경 설정 조회
        configApis.add(createDefaultHeaders(new ApiRequest("/admin/config/environment", "GET"))
                .addQueryParam("env", env));
        
        // 설정 업데이트
        configApis.add(createDefaultHeaders(new ApiRequest("/admin/config/update", "PUT"))
                .setBody("{\"maxConnections\":100,\"timeout\":30,\"retryCount\":3}"));
        
        // 설정 백업
        configApis.add(createDefaultHeaders(new ApiRequest("/admin/config/backup", "POST"))
                .setBody("{\"backupName\":\"config_backup_2024\",\"description\":\"Yearly configuration backup\"}"));
        
        executeConcurrentTests(configApis);
        logger.info("--- 시스템 설정 API 테스트 완료 ---");
    }

    // 알림 관리 API 테스트
    public void testNotificationApis() {
        logger.info("--- 알림 관리 API 테스트 시작 ---");
        
        List<ApiRequest> notificationApis = new ArrayList<>();
        
        // 알림 목록 조회
        notificationApis.add(createDefaultHeaders(new ApiRequest("/admin/notifications", "GET"))
                .addQueryParam("page", "1")
                .addQueryParam("size", "50")
                .addQueryParam("status", "UNREAD"));
        
        // 알림 전송
        notificationApis.add(createDefaultHeaders(new ApiRequest("/admin/notifications/send", "POST"))
                .setBody("{\"type\":\"SYSTEM\",\"title\":\"시스템 점검 알림\",\"message\":\"정기 시스템 점검이 예정되어 있습니다.\",\"recipients\":[\"all\"]}"));
        
        // 알림 설정 조회
        notificationApis.add(createDefaultHeaders(new ApiRequest("/admin/notifications/settings", "GET")));
        
        // 알림 설정 업데이트
        notificationApis.add(createDefaultHeaders(new ApiRequest("/admin/notifications/settings", "PUT"))
                .setBody("{\"emailEnabled\":true,\"smsEnabled\":false,\"pushEnabled\":true,\"quietHours\":{\"start\":\"22:00\",\"end\":\"08:00\"}}"));
        
        executeConcurrentTests(notificationApis);
        logger.info("--- 알림 관리 API 테스트 완료 ---");
    }

    // 감사 로그 API 테스트
    public void testAuditLogApis() {
        logger.info("--- 감사 로그 API 테스트 시작 ---");
        
        List<ApiRequest> auditApis = new ArrayList<>();
        
        // 감사 로그 조회
        auditApis.add(createDefaultHeaders(new ApiRequest("/admin/audit/logs", "GET"))
                .addQueryParam("startDate", "2024-01-01")
                .addQueryParam("endDate", "2024-12-31")
                .addQueryParam("action", "LOGIN")
                .addQueryParam("page", "1")
                .addQueryParam("size", "100"));
        
        // 감사 로그 상세 조회
        auditApis.add(createDefaultHeaders(new ApiRequest("/admin/audit/logs/detail", "GET"))
                .addQueryParam("logId", "AUDIT_001"));
        
        // 감사 로그 내보내기
        auditApis.add(createDefaultHeaders(new ApiRequest("/admin/audit/export", "POST"))
                .setBody("{\"format\":\"CSV\",\"startDate\":\"2024-01-01\",\"endDate\":\"2024-12-31\",\"filters\":{\"action\":\"LOGIN\",\"userId\":\"USER_001\"}}"));
        
        // 감사 정책 조회
        auditApis.add(createDefaultHeaders(new ApiRequest("/admin/audit/policy", "GET")));
        
        executeConcurrentTests(auditApis);
        logger.info("--- 감사 로그 API 테스트 완료 ---");
    }

    // 응답 데이터 분석 및 로깅
    public void analyzeResponses() {
        logger.info("=== 관리 시스템 응답 데이터 분석 ===");
        
        for (TestResult result : results) {
            if (result.isSuccess() && result.getApiResponse() != null) {
                ApiResponse response = result.getApiResponse();
                
                logger.info("API: {}", result.getApi());
                logger.info("  - 상태 코드: {}", response.getStatusCode());
                logger.info("  - 응답 시간: {}ms", response.getResponseTime());
                logger.info("  - API ID: {}", response.getApiId());
                logger.info("  - 응답 코드: {}", response.getCode());
                logger.info("  - 메시지: {}", response.getMessage());
                logger.info("  - 서버 시간: {}", response.getServerTime());
                logger.info("  - Trace ID: {}", response.getTraceId());
                
                // 데이터 필드 분석
                if (response.getData() != null) {
                    logger.info("  - 데이터 존재: true");
                    List<JsonNode> dataList = response.getDataList();
                    if (!dataList.isEmpty()) {
                        logger.info("  - 데이터 개수: {}", dataList.size());
                        // 첫 번째 데이터 항목의 필드들 출력
                        JsonNode firstItem = dataList.get(0);
                        firstItem.fieldNames().forEachRemaining(fieldName -> {
                            logger.info("    - {}: {}", fieldName, firstItem.get(fieldName));
                        });
                    }
                } else {
                    logger.info("  - 데이터 존재: false");
                }
                logger.info("---");
            }
        }
    }
} 