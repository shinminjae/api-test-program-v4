package com.daedong.agmtms.test.api.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiRequest {
    private String endpoint;
    private String method;
    private Map<String, String> headers;
    private Map<String, String> queryParams;
    private Map<String, String> pathParams;
    private String body;
    private Map<String, Object> multipartData;
    private String contentType;

    public ApiRequest(String endpoint, String method) {
        this.endpoint = endpoint;
        this.method = method;
        this.headers = new HashMap<>();
        this.queryParams = new HashMap<>();
        this.pathParams = new HashMap<>();
        this.multipartData = new HashMap<>();
        this.contentType = "application/json";
    }

    public ApiRequest(String endpoint, String method, Map<String, String> headers, String body) {
        this(endpoint, method);
        if (headers != null) this.headers.putAll(headers);
        this.body = body;
    }

    // 헤더 설정 메서드들
    public ApiRequest addHeader(String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    public ApiRequest addHeaders(Map<String, String> headers) {
        if (headers != null) this.headers.putAll(headers);
        return this;
    }

    // 쿼리 파라미터 설정 메서드들
    public ApiRequest addQueryParam(String key, String value) {
        this.queryParams.put(key, value);
        return this;
    }

    public ApiRequest addQueryParams(Map<String, String> params) {
        if (params != null) this.queryParams.putAll(params);
        return this;
    }

    // Path 파라미터 설정 메서드들
    public ApiRequest addPathParam(String key, String value) {
        this.pathParams.put(key, value);
        return this;
    }

    public ApiRequest addPathParams(Map<String, String> params) {
        if (params != null) this.pathParams.putAll(params);
        return this;
    }

    // 바디 설정 메서드들
    public ApiRequest setBody(String body) {
        this.body = body;
        return this;
    }

    public ApiRequest setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    // 멀티파트 데이터 설정 메서드들
    public ApiRequest addMultipartField(String key, String value) {
        this.multipartData.put(key, value);
        return this;
    }

    public ApiRequest addMultipartFile(String key, Object file) {
        this.multipartData.put(key, file);
        return this;
    }

    public ApiRequest setMultipartData(Map<String, Object> multipartData) {
        this.multipartData = multipartData;
        return this;
    }

    // Getter 메서드들
    public String getEndpoint() { return endpoint; }
    public String getMethod() { return method; }
    public Map<String, String> getHeaders() { return headers; }
    public Map<String, String> getQueryParams() { return queryParams; }
    public Map<String, String> getPathParams() { return pathParams; }
    public String getBody() { return body; }
    public Map<String, Object> getMultipartData() { return multipartData; }
    public String getContentType() { return contentType; }

    // Path 파라미터가 적용된 엔드포인트 생성
    public String getResolvedEndpoint() {
        String resolvedEndpoint = endpoint;
        
        // 디버깅을 위한 로그 추가
        if (!pathParams.isEmpty()) {
            System.out.println("=== Path 파라미터 치환 과정 ===");
            System.out.println("원본 엔드포인트: " + endpoint);
            System.out.println("설정된 Path 파라미터: " + pathParams);
        }
        
        for (Map.Entry<String, String> entry : pathParams.entrySet()) {
            String placeholder = "{" + entry.getKey() + "}";
            String value = entry.getValue();
            
            System.out.println("치환: " + placeholder + " -> " + value);
            resolvedEndpoint = resolvedEndpoint.replace(placeholder, value);
            System.out.println("치환 후: " + resolvedEndpoint);
        }
        
        // 치환되지 않은 플레이스홀더가 있는지 확인
        if (resolvedEndpoint.contains("{")) {
            System.err.println("경고: 치환되지 않은 Path 파라미터가 있습니다: " + resolvedEndpoint);
        }
        
        System.out.println("최종 엔드포인트: " + resolvedEndpoint);
        System.out.println("=== Path 파라미터 치환 완료 ===");
        
        return resolvedEndpoint;
    }

    // URL에 쿼리 파라미터 추가
    public String getFullUrl(String baseUrl) {
        String resolvedEndpoint = getResolvedEndpoint();
        String url = baseUrl + resolvedEndpoint;
        
        if (queryParams.isEmpty()) {
            return url;
        }
        
        StringBuilder fullUrl = new StringBuilder(url + "?");
        queryParams.forEach((key, value) -> 
            fullUrl.append(key).append("=").append(value).append("&"));
        
        // 마지막 & 제거
        return fullUrl.substring(0, fullUrl.length() - 1);
    }

    public boolean isMultipart() {
        return "multipart/form-data".equals(contentType) || !multipartData.isEmpty();
    }

    // Path 파라미터가 있는지 확인
    public boolean hasPathParams() {
        return !pathParams.isEmpty();
    }

    // Path 파라미터 템플릿에서 필요한 파라미터 추출
    public static Map<String, String> extractPathParamsFromTemplate(String template) {
        Map<String, String> requiredParams = new HashMap<>();
        Pattern pattern = Pattern.compile("\\{([^}]+)\\}");
        Matcher matcher = pattern.matcher(template);
        
        while (matcher.find()) {
            String paramName = matcher.group(1);
            requiredParams.put(paramName, "");
        }
        
        return requiredParams;
    }

    // Path 파라미터 검증
    public boolean validatePathParams() {
        Map<String, String> requiredParams = extractPathParamsFromTemplate(endpoint);
        
        System.out.println("=== Path 파라미터 검증 ===");
        System.out.println("템플릿: " + endpoint);
        System.out.println("필요한 파라미터: " + requiredParams.keySet());
        System.out.println("설정된 파라미터: " + pathParams.keySet());
        
        for (String requiredParam : requiredParams.keySet()) {
            if (!pathParams.containsKey(requiredParam)) {
                System.err.println("오류: 필수 Path 파라미터가 누락되었습니다: " + requiredParam);
                return false;
            }
            
            String value = pathParams.get(requiredParam);
            if (value == null || value.trim().isEmpty()) {
                System.err.println("오류: Path 파라미터 값이 비어있습니다: " + requiredParam);
                return false;
            }
            
            System.out.println("✓ " + requiredParam + " = " + value);
        }
        
        System.out.println("=== Path 파라미터 검증 완료 (성공) ===");
        return true;
    }
}
