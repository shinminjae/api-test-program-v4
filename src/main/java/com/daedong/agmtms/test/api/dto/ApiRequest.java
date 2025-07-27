package com.daedong.agmtms.test.api.dto;

import java.util.Map;

public class ApiRequest {
    private String endpoint;
    private String method;
    private Map<String, String> headers;
    private String body;

    public ApiRequest(String endpoint, String method, Map<String, String> headers, String body) {
        this.endpoint = endpoint;
        this.method = method;
        this.headers = headers;
        this.body = body;
    }

    public String getEndpoint() { return endpoint; }
    public String getMethod() { return method; }
    public Map<String, String> getHeaders() { return headers; }
    public String getBody() { return body; }
}
