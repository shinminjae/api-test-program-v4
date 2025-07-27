package com.daedong.agmtms.test.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenResponse {
    private boolean success;
    private Data data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        private String accessToken;
        private String refreshToken;

        public String getAccessToken() { return accessToken; }
        public String getRefreshToken() { return refreshToken; }
    }

    public Data getData() { return data; }
    public String getAccessToken() { return data != null ? data.accessToken : null; }
}
