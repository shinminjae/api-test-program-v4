package com.daedong.agmtms.test.api;

import com.daedong.agmtms.test.api.dto.ApiRequest;
import com.daedong.agmtms.test.api.dto.ApiResponse;
import com.daedong.agmtms.test.api.dto.TestResult;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MobileApiTestService extends BaseApiTestService {
    private static final Logger logger = LoggerFactory.getLogger(MobileApiTestService.class);

    public MobileApiTestService(String env, String token) {
        super(env, token);
    }

    @Override
    public void testTmsAdcu() {
        logger.info("=== 모바일 API 전체 테스트 시작 ===");
        
        // 각 카테고리별 테스트 실행
        testMobileVehicleApis();
        testMobileWorkApis();
        testMobileLocationApis();
        testMobileNotificationApis();
        testMobileSyncApis();
        
        logger.info("=== 모바일 API 전체 테스트 완료 ===");
    }

    // 모바일 차량 관련 API 테스트
    public void testMobileVehicleApis() {
        logger.info("--- 모바일 차량 API 테스트 시작 ---");
        
        List<ApiRequest> mobileVehicleApis = new ArrayList<>();
        
        // 차량 상태 조회 (모바일용)
        mobileVehicleApis.add(createDefaultHeaders(new ApiRequest("/mobile/vehicle/status", "GET"))
                .addQueryParam("vinId", "TEST_VIN_001")
                .addHeader("X-Device-Type", "MOBILE")
                .addHeader("X-App-Version", "1.0.0"));
        
        // 차량 상세 정보 조회 (Path 파라미터 사용)
        mobileVehicleApis.add(createDefaultHeaders(new ApiRequest("/mobile/vehicle/{vinId}/status", "GET"))
                .addPathParam("vinId", "TEST_VIN_001")
                .addHeader("X-Device-Type", "MOBILE")
                .addHeader("X-App-Version", "1.0.0"));
        
        // 차량 제어 명령 전송 (Path 파라미터 사용)
        mobileVehicleApis.add(createDefaultHeaders(new ApiRequest("/mobile/vehicle/{vinId}/control", "POST"))
                .addPathParam("vinId", "TEST_VIN_001")
                .addHeader("X-Device-Type", "MOBILE")
                .setBody("{\"command\":\"START_ENGINE\",\"parameters\":{\"warmup\":true}}"));
        
        // 차량 실시간 데이터 조회 (Path 파라미터 사용)
        mobileVehicleApis.add(createDefaultHeaders(new ApiRequest("/mobile/vehicle/{vinId}/realtime", "GET"))
                .addPathParam("vinId", "TEST_VIN_001")
                .addQueryParam("dataType", "ALL")
                .addHeader("X-Device-Type", "MOBILE"));
        
        // 차량 알람 설정 (Path 파라미터 사용)
        mobileVehicleApis.add(createDefaultHeaders(new ApiRequest("/mobile/vehicle/{vinId}/alarm", "POST"))
                .addPathParam("vinId", "TEST_VIN_001")
                .addHeader("X-Device-Type", "MOBILE")
                .setBody("{\"alarmType\":\"MAINTENANCE\",\"threshold\":5000,\"enabled\":true}"));
        
        // 차량 알람 설정 조회 (Path 파라미터 사용)
        mobileVehicleApis.add(createDefaultHeaders(new ApiRequest("/mobile/vehicle/{vinId}/alarm", "GET"))
                .addPathParam("vinId", "TEST_VIN_001")
                .addHeader("X-Device-Type", "MOBILE"));
        
        // 차량 알람 설정 수정 (Path 파라미터 사용)
        mobileVehicleApis.add(createDefaultHeaders(new ApiRequest("/mobile/vehicle/{vinId}/alarm/{alarmId}", "PUT"))
                .addPathParam("vinId", "TEST_VIN_001")
                .addPathParam("alarmId", "ALARM_001")
                .addHeader("X-Device-Type", "MOBILE")
                .setBody("{\"threshold\":6000,\"enabled\":false}"));
        
        // 차량 알람 설정 삭제 (Path 파라미터 사용)
        mobileVehicleApis.add(createDefaultHeaders(new ApiRequest("/mobile/vehicle/{vinId}/alarm/{alarmId}", "DELETE"))
                .addPathParam("vinId", "TEST_VIN_001")
                .addPathParam("alarmId", "ALARM_002")
                .addHeader("X-Device-Type", "MOBILE"));
        
        executeConcurrentTests(mobileVehicleApis);
        logger.info("--- 모바일 차량 API 테스트 완료 ---");
    }

    // 모바일 작업 관련 API 테스트
    public void testMobileWorkApis() {
        logger.info("--- 모바일 작업 API 테스트 시작 ---");
        
        List<ApiRequest> mobileWorkApis = new ArrayList<>();
        
        // 작업 목록 조회 (모바일용)
        mobileWorkApis.add(createDefaultHeaders(new ApiRequest("/mobile/work/list", "GET"))
                .addQueryParam("status", "ACTIVE")
                .addQueryParam("page", "1")
                .addQueryParam("size", "20")
                .addHeader("X-Device-Type", "MOBILE"));
        
        // 작업 상세 정보 조회 (Path 파라미터 사용)
        mobileWorkApis.add(createDefaultHeaders(new ApiRequest("/mobile/work/{workId}", "GET"))
                .addPathParam("workId", "WORK_001")
                .addHeader("X-Device-Type", "MOBILE"));
        
        // 작업 시작 (Path 파라미터 사용)
        mobileWorkApis.add(createDefaultHeaders(new ApiRequest("/mobile/work/{workId}/start", "POST"))
                .addPathParam("workId", "WORK_001")
                .addHeader("X-Device-Type", "MOBILE")
                .setBody("{\"startTime\":\"2024-01-15T09:00:00Z\",\"location\":{\"lat\":37.5665,\"lng\":126.9780}}"));
        
        // 작업 진행 상황 업데이트 (Path 파라미터 사용)
        mobileWorkApis.add(createDefaultHeaders(new ApiRequest("/mobile/work/{workId}/progress", "PUT"))
                .addPathParam("workId", "WORK_001")
                .addHeader("X-Device-Type", "MOBILE")
                .setBody("{\"progress\":75,\"currentArea\":\"FIELD_A\",\"notes\":\"작업 진행 중\"}"));
        
        // 작업 완료 (Path 파라미터 사용)
        mobileWorkApis.add(createDefaultHeaders(new ApiRequest("/mobile/work/{workId}/complete", "POST"))
                .addPathParam("workId", "WORK_001")
                .addHeader("X-Device-Type", "MOBILE")
                .setBody("{\"endTime\":\"2024-01-15T17:00:00Z\",\"result\":\"SUCCESS\",\"totalArea\":100.5}"));
        
        // 작업 취소 (Path 파라미터 사용)
        mobileWorkApis.add(createDefaultHeaders(new ApiRequest("/mobile/work/{workId}/cancel", "POST"))
                .addPathParam("workId", "WORK_002")
                .addHeader("X-Device-Type", "MOBILE")
                .setBody("{\"reason\":\"날씨 악화\",\"cancelTime\":\"2024-01-15T14:00:00Z\"}"));
        
        // 작업 이력 조회 (Path 파라미터 사용)
        mobileWorkApis.add(createDefaultHeaders(new ApiRequest("/mobile/work/{workId}/history", "GET"))
                .addPathParam("workId", "WORK_001")
                .addHeader("X-Device-Type", "MOBILE")
                .addQueryParam("includeDetails", "true"));
        
        executeConcurrentTests(mobileWorkApis);
        logger.info("--- 모바일 작업 API 테스트 완료 ---");
    }

    // 모바일 위치 관련 API 테스트
    public void testMobileLocationApis() {
        logger.info("--- 모바일 위치 API 테스트 시작 ---");
        
        List<ApiRequest> mobileLocationApis = new ArrayList<>();
        
        // 위치 정보 전송
        mobileLocationApis.add(createDefaultHeaders(new ApiRequest("/mobile/location/update", "POST"))
                .addHeader("X-Device-Type", "MOBILE")
                .setBody("{\"vinId\":\"TEST_VIN_001\",\"latitude\":37.5665,\"longitude\":126.9780,\"altitude\":50,\"speed\":25.5,\"heading\":180,\"timestamp\":\"2024-01-15T10:30:00Z\"}"));
        
        // 경로 기록 시작
        mobileLocationApis.add(createDefaultHeaders(new ApiRequest("/mobile/location/track/start", "POST"))
                .addHeader("X-Device-Type", "MOBILE")
                .setBody("{\"vinId\":\"TEST_VIN_001\",\"trackId\":\"TRACK_001\",\"startLocation\":{\"lat\":37.5665,\"lng\":126.9780}}"));
        
        // 경로 포인트 추가
        mobileLocationApis.add(createDefaultHeaders(new ApiRequest("/mobile/location/track/point", "POST"))
                .addHeader("X-Device-Type", "MOBILE")
                .setBody("{\"trackId\":\"TRACK_001\",\"points\":[{\"lat\":37.5666,\"lng\":126.9781,\"timestamp\":\"2024-01-15T10:31:00Z\"},{\"lat\":37.5667,\"lng\":126.9782,\"timestamp\":\"2024-01-15T10:32:00Z\"}]}"));
        
        // 경로 기록 종료
        mobileLocationApis.add(createDefaultHeaders(new ApiRequest("/mobile/location/track/end", "POST"))
                .addHeader("X-Device-Type", "MOBILE")
                .setBody("{\"trackId\":\"TRACK_001\",\"endLocation\":{\"lat\":37.5670,\"lng\":126.9785},\"totalDistance\":1500.5}"));
        
        executeConcurrentTests(mobileLocationApis);
        logger.info("--- 모바일 위치 API 테스트 완료 ---");
    }

    // 모바일 알림 관련 API 테스트
    public void testMobileNotificationApis() {
        logger.info("--- 모바일 알림 API 테스트 시작 ---");
        
        List<ApiRequest> mobileNotificationApis = new ArrayList<>();
        
        // 푸시 토큰 등록
        mobileNotificationApis.add(createDefaultHeaders(new ApiRequest("/mobile/notification/token", "POST"))
                .addHeader("X-Device-Type", "MOBILE")
                .setBody("{\"deviceToken\":\"FCM_TOKEN_123\",\"deviceType\":\"ANDROID\",\"appVersion\":\"1.0.0\"}"));
        
        // 알림 설정 조회
        mobileNotificationApis.add(createDefaultHeaders(new ApiRequest("/mobile/notification/settings", "GET"))
                .addHeader("X-Device-Type", "MOBILE"));
        
        // 알림 설정 업데이트
        mobileNotificationApis.add(createDefaultHeaders(new ApiRequest("/mobile/notification/settings", "PUT"))
                .addHeader("X-Device-Type", "MOBILE")
                .setBody("{\"pushEnabled\":true,\"soundEnabled\":true,\"vibrationEnabled\":true,\"categories\":{\"work\":true,\"maintenance\":true,\"system\":false}}"));
        
        // 알림 읽음 처리
        mobileNotificationApis.add(createDefaultHeaders(new ApiRequest("/mobile/notification/read", "POST"))
                .addHeader("X-Device-Type", "MOBILE")
                .setBody("{\"notificationIds\":[\"NOTIF_001\",\"NOTIF_002\"]}"));
        
        executeConcurrentTests(mobileNotificationApis);
        logger.info("--- 모바일 알림 API 테스트 완료 ---");
    }

    // 모바일 동기화 관련 API 테스트
    public void testMobileSyncApis() {
        logger.info("--- 모바일 동기화 API 테스트 시작 ---");
        
        List<ApiRequest> mobileSyncApis = new ArrayList<>();
        
        // 데이터 동기화 상태 확인
        mobileSyncApis.add(createDefaultHeaders(new ApiRequest("/mobile/sync/status", "GET"))
                .addQueryParam("lastSyncTime", "2024-01-15T08:00:00Z")
                .addHeader("X-Device-Type", "MOBILE"));
        
        // 오프라인 데이터 업로드
        mobileSyncApis.add(createDefaultHeaders(new ApiRequest("/mobile/sync/upload", "POST"))
                .addHeader("X-Device-Type", "MOBILE")
                .setBody("{\"dataType\":\"WORK_LOG\",\"records\":[{\"id\":\"LOCAL_001\",\"workId\":\"WORK_001\",\"startTime\":\"2024-01-15T09:00:00Z\",\"endTime\":\"2024-01-15T17:00:00Z\",\"area\":100.5}]}"));
        
        // 설정 동기화
        mobileSyncApis.add(createDefaultHeaders(new ApiRequest("/mobile/sync/settings", "GET"))
                .addQueryParam("lastUpdate", "2024-01-15T08:00:00Z")
                .addHeader("X-Device-Type", "MOBILE"));
        
        // 파일 동기화
        mobileSyncApis.add(createDefaultHeaders(new ApiRequest("/mobile/sync/files", "POST"))
                .addHeader("X-Device-Type", "MOBILE")
                .setBody("{\"files\":[{\"name\":\"work_report.pdf\",\"size\":1024000,\"hash\":\"abc123def456\"}]}"));
        
        executeConcurrentTests(mobileSyncApis);
        logger.info("--- 모바일 동기화 API 테스트 완료 ---");
    }

    // 응답 데이터 분석 및 로깅
    public void analyzeResponses() {
        logger.info("=== 모바일 API 응답 데이터 분석 ===");
        
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