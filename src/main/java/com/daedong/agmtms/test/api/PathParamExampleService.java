package com.daedong.agmtms.test.api;

import com.daedong.agmtms.test.api.dto.ApiRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Path 파라미터 사용법을 보여주는 예제 서비스
 */
public class PathParamExampleService extends BaseApiTestService {
    private static final Logger logger = LoggerFactory.getLogger(PathParamExampleService.class);

    public PathParamExampleService(String env, String token) {
        super(env, token);
    }

    @Override
    public void testTmsAdcu() {
        logger.info("=== Path 파라미터 예제 API 테스트 시작 ===");
        
        testBasicPathParams();
        testMultiplePathParams();
        testPathParamValidation();
        testComplexPathParams();
        
        logger.info("=== Path 파라미터 예제 API 테스트 완료 ===");
    }

    // 기본 Path 파라미터 예제
    public void testBasicPathParams() {
        logger.info("--- 기본 Path 파라미터 예제 ---");
        
        List<ApiRequest> basicPathApis = new ArrayList<>();
        
        // 단일 Path 파라미터
        basicPathApis.add(createDefaultHeaders(new ApiRequest("/api/users/{userId}", "GET"))
                .addPathParam("userId", "USER_001"));
        
        // Path 파라미터 + 쿼리 파라미터
        basicPathApis.add(createDefaultHeaders(new ApiRequest("/api/users/{userId}/posts", "GET"))
                .addPathParam("userId", "USER_001")
                .addQueryParam("page", "1")
                .addQueryParam("size", "10"));
        
        // Path 파라미터 + PUT 요청
        basicPathApis.add(createDefaultHeaders(new ApiRequest("/api/users/{userId}", "PUT"))
                .addPathParam("userId", "USER_001")
                .setBody("{\"name\":\"Updated User\",\"email\":\"updated@example.com\"}"));
        
        // Path 파라미터 + DELETE 요청
        basicPathApis.add(createDefaultHeaders(new ApiRequest("/api/users/{userId}", "DELETE"))
                .addPathParam("userId", "USER_002"));
        
        executeConcurrentTests(basicPathApis);
        logger.info("--- 기본 Path 파라미터 예제 완료 ---");
    }

    // 다중 Path 파라미터 예제
    public void testMultiplePathParams() {
        logger.info("--- 다중 Path 파라미터 예제 ---");
        
        List<ApiRequest> multiplePathApis = new ArrayList<>();
        
        // 두 개의 Path 파라미터
        multiplePathApis.add(createDefaultHeaders(new ApiRequest("/api/users/{userId}/posts/{postId}", "GET"))
                .addPathParam("userId", "USER_001")
                .addPathParam("postId", "POST_001"));
        
        // 세 개의 Path 파라미터
        multiplePathApis.add(createDefaultHeaders(new ApiRequest("/api/users/{userId}/posts/{postId}/comments/{commentId}", "GET"))
                .addPathParam("userId", "USER_001")
                .addPathParam("postId", "POST_001")
                .addPathParam("commentId", "COMMENT_001"));
        
        // 다중 Path 파라미터 + PUT 요청
        multiplePathApis.add(createDefaultHeaders(new ApiRequest("/api/users/{userId}/posts/{postId}", "PUT"))
                .addPathParam("userId", "USER_001")
                .addPathParam("postId", "POST_001")
                .setBody("{\"title\":\"Updated Post\",\"content\":\"Updated content\"}"));
        
        // 다중 Path 파라미터 + DELETE 요청
        multiplePathApis.add(createDefaultHeaders(new ApiRequest("/api/users/{userId}/posts/{postId}", "DELETE"))
                .addPathParam("userId", "USER_001")
                .addPathParam("postId", "POST_002"));
        
        executeConcurrentTests(multiplePathApis);
        logger.info("--- 다중 Path 파라미터 예제 완료 ---");
    }

    // Path 파라미터 검증 예제
    public void testPathParamValidation() {
        logger.info("--- Path 파라미터 검증 예제 ---");
        
        // 템플릿에서 필요한 Path 파라미터 추출
        String template = "/api/users/{userId}/posts/{postId}/comments/{commentId}";
        Map<String, String> requiredParams = ApiRequest.extractPathParamsFromTemplate(template);
        
        logger.info("템플릿: {}", template);
        logger.info("필요한 Path 파라미터: {}", requiredParams.keySet());
        
        // 올바른 Path 파라미터 설정
        ApiRequest validRequest = new ApiRequest(template, "GET")
                .addPathParam("userId", "USER_001")
                .addPathParam("postId", "POST_001")
                .addPathParam("commentId", "COMMENT_001");
        
        boolean isValid = validRequest.validatePathParams();
        logger.info("올바른 Path 파라미터 검증 결과: {}", isValid);
        
        // 잘못된 Path 파라미터 설정 (누락된 파라미터)
        ApiRequest invalidRequest = new ApiRequest(template, "GET")
                .addPathParam("userId", "USER_001")
                .addPathParam("postId", "POST_001");
        // commentId 누락
        
        boolean isInvalid = invalidRequest.validatePathParams();
        logger.info("잘못된 Path 파라미터 검증 결과: {}", isInvalid);
        
        // 실제 API 호출 테스트
        List<ApiRequest> validationApis = new ArrayList<>();
        validationApis.add(createDefaultHeaders(validRequest));
        
        executeConcurrentTests(validationApis);
        logger.info("--- Path 파라미터 검증 예제 완료 ---");
    }

    // 복잡한 Path 파라미터 예제
    public void testComplexPathParams() {
        logger.info("--- 복잡한 Path 파라미터 예제 ---");
        
        List<ApiRequest> complexPathApis = new ArrayList<>();
        
        // RESTful API 패턴
        complexPathApis.add(createDefaultHeaders(new ApiRequest("/api/organizations/{orgId}/departments/{deptId}/employees/{empId}", "GET"))
                .addPathParam("orgId", "ORG_001")
                .addPathParam("deptId", "DEPT_001")
                .addPathParam("empId", "EMP_001")
                .addQueryParam("includeDetails", "true"));
        
        // 버전 관리가 포함된 API
        complexPathApis.add(createDefaultHeaders(new ApiRequest("/api/v{version}/users/{userId}/profile", "GET"))
                .addPathParam("version", "2")
                .addPathParam("userId", "USER_001"));
        
        // 다국어 지원이 포함된 API
        complexPathApis.add(createDefaultHeaders(new ApiRequest("/api/{locale}/content/{contentId}", "GET"))
                .addPathParam("locale", "ko-KR")
                .addPathParam("contentId", "CONTENT_001"));
        
        // 카테고리 기반 API
        complexPathApis.add(createDefaultHeaders(new ApiRequest("/api/categories/{categoryId}/products/{productId}/reviews/{reviewId}", "GET"))
                .addPathParam("categoryId", "CAT_001")
                .addPathParam("productId", "PROD_001")
                .addPathParam("reviewId", "REVIEW_001")
                .addQueryParam("sort", "date")
                .addQueryParam("order", "desc"));
        
        // 동적 리소스 경로
        complexPathApis.add(createDefaultHeaders(new ApiRequest("/api/files/{filePath}", "GET"))
                .addPathParam("filePath", "documents/reports/2024/annual-report.pdf"));
        
        executeConcurrentTests(complexPathApis);
        logger.info("--- 복잡한 Path 파라미터 예제 완료 ---");
    }

    // Path 파라미터 사용법 가이드
    public void showPathParamUsageGuide() {
        logger.info("=== Path 파라미터 사용법 가이드 ===");
        
        logger.info("1. 기본 사용법:");
        logger.info("   ApiRequest request = new ApiRequest(\"/api/users/{userId}\", \"GET\")");
        logger.info("       .addPathParam(\"userId\", \"USER_001\");");
        
        logger.info("2. 다중 Path 파라미터:");
        logger.info("   ApiRequest request = new ApiRequest(\"/api/users/{userId}/posts/{postId}\", \"GET\")");
        logger.info("       .addPathParam(\"userId\", \"USER_001\")");
        logger.info("       .addPathParam(\"postId\", \"POST_001\");");
        
        logger.info("3. Path 파라미터 + 쿼리 파라미터:");
        logger.info("   ApiRequest request = new ApiRequest(\"/api/users/{userId}/posts\", \"GET\")");
        logger.info("       .addPathParam(\"userId\", \"USER_001\")");
        logger.info("       .addQueryParam(\"page\", \"1\")");
        logger.info("       .addQueryParam(\"size\", \"10\");");
        
        logger.info("4. Path 파라미터 검증:");
        logger.info("   String template = \"/api/users/{userId}/posts/{postId}\";");
        logger.info("   Map<String, String> requiredParams = ApiRequest.extractPathParamsFromTemplate(template);");
        logger.info("   boolean isValid = request.validatePathParams();");
        
        logger.info("5. 실제 URL 생성:");
        logger.info("   String fullUrl = request.getFullUrl(\"https://api.example.com\");");
        logger.info("   // 결과: https://api.example.com/api/users/USER_001/posts/POST_001?page=1&size=10");
        
        logger.info("=== Path 파라미터 사용법 가이드 완료 ===");
    }
} 