package com.daedong.agmtms.test.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Map;

public class TestConfig {
    private static final Logger logger = LoggerFactory.getLogger(TestConfig.class);
    private static Map<String, Object> config;

    static {
        try (InputStream is = TestConfig.class.getClassLoader().getResourceAsStream("application.yml")) {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            config = mapper.readValue(is, Map.class);
        } catch (Exception e) {
            logger.error("설정 파일 로드 실패", e);
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> getEnvConfig(String env) {
        Map<String, Object> envs = (Map<String, Object>) config.get("environments");
        return (Map<String, Object>) envs.get(env);
    }

    public static Map<String, Object> getTestConfig() {
        return (Map<String, Object>) config.get("test-config");
    }
}
