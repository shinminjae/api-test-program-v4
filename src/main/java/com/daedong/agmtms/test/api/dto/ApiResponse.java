package com.daedong.agmtms.test.api.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApiResponse {
    private static final Logger logger = LoggerFactory.getLogger(ApiResponse.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final int statusCode;
    private final String rawResponse;
    private final JsonNode jsonNode;
    private final boolean isSuccess;
    private final long responseTime;

    public ApiResponse(int statusCode, String rawResponse, long responseTime) {
        this.statusCode = statusCode;
        this.rawResponse = rawResponse;
        this.responseTime = responseTime;
        this.isSuccess = statusCode >= 200 && statusCode < 300;
        
        JsonNode tempNode = null;
        if (rawResponse != null && !rawResponse.trim().isEmpty()) {
            try {
                tempNode = objectMapper.readTree(rawResponse);
            } catch (IOException e) {
                logger.warn("JSON 파싱 실패: {}", e.getMessage());
            }
        }
        this.jsonNode = tempNode;
    }

    // 기본 응답 정보
    public int getStatusCode() { return statusCode; }
    public String getRawResponse() { return rawResponse; }
    public boolean isSuccess() { return isSuccess; }
    public long getResponseTime() { return responseTime; }
    public JsonNode getJsonNode() { return jsonNode; }

    // JSON 응답에서 개별 필드 추출
    public String getString(String fieldName) {
        return getString(fieldName, null);
    }

    public String getString(String fieldName, String defaultValue) {
        if (jsonNode == null) return defaultValue;
        JsonNode node = jsonNode.get(fieldName);
        return node != null ? node.asText() : defaultValue;
    }

    public Integer getInteger(String fieldName) {
        return getInteger(fieldName, null);
    }

    public Integer getInteger(String fieldName, Integer defaultValue) {
        if (jsonNode == null) return defaultValue;
        JsonNode node = jsonNode.get(fieldName);
        return node != null ? node.asInt() : defaultValue;
    }

    public Long getLong(String fieldName) {
        return getLong(fieldName, null);
    }

    public Long getLong(String fieldName, Long defaultValue) {
        if (jsonNode == null) return defaultValue;
        JsonNode node = jsonNode.get(fieldName);
        return node != null ? node.asLong() : defaultValue;
    }

    public Boolean getBoolean(String fieldName) {
        return getBoolean(fieldName, null);
    }

    public Boolean getBoolean(String fieldName, Boolean defaultValue) {
        if (jsonNode == null) return defaultValue;
        JsonNode node = jsonNode.get(fieldName);
        return node != null ? node.asBoolean() : defaultValue;
    }

    public JsonNode getNode(String fieldName) {
        if (jsonNode == null) return null;
        return jsonNode.get(fieldName);
    }

    public List<JsonNode> getArray(String fieldName) {
        List<JsonNode> result = new ArrayList<>();
        if (jsonNode == null) return result;
        
        JsonNode arrayNode = jsonNode.get(fieldName);
        if (arrayNode != null && arrayNode.isArray()) {
            for (JsonNode node : arrayNode) {
                result.add(node);
            }
        }
        return result;
    }

    // 중첩된 필드 접근 (예: "data.list")
    public String getNestedString(String path) {
        return getNestedString(path, null);
    }

    public String getNestedString(String path, String defaultValue) {
        JsonNode node = getNestedNode(path);
        return node != null ? node.asText() : defaultValue;
    }

    public Integer getNestedInteger(String path) {
        return getNestedInteger(path, null);
    }

    public Integer getNestedInteger(String path, Integer defaultValue) {
        JsonNode node = getNestedNode(path);
        return node != null ? node.asInt() : defaultValue;
    }

    public Long getNestedLong(String path) {
        return getNestedLong(path, null);
    }

    public Long getNestedLong(String path, Long defaultValue) {
        JsonNode node = getNestedNode(path);
        return node != null ? node.asLong() : defaultValue;
    }

    private JsonNode getNestedNode(String path) {
        if (jsonNode == null || path == null) return null;
        
        String[] parts = path.split("\\.");
        JsonNode current = jsonNode;
        
        for (String part : parts) {
            if (current == null) return null;
            current = current.get(part);
        }
        
        return current;
    }

    // API 응답 구조에 맞는 편의 메서드들
    public String getApiId() {
        return getString("apiId");
    }

    public Integer getCode() {
        return getInteger("code");
    }

    public String getMessage() {
        return getString("message");
    }

    public Long getServerTime() {
        return getLong("serverTime");
    }

    public String getTraceId() {
        return getString("traceId");
    }

    public JsonNode getData() {
        return getNode("data");
    }

    public List<JsonNode> getDataList() {
        return getArray("data");
    }

    @Override
    public String toString() {
        return String.format("ApiResponse{statusCode=%d, success=%s, responseTime=%dms, data=%s}", 
                statusCode, isSuccess, responseTime, 
                jsonNode != null ? jsonNode.toString() : rawResponse);
    }
} 