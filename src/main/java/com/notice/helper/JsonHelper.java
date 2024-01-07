package com.notice.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

/**
 * <pre>
 *     JSON 관련 편의 클래스
 *
 *     Spring에서 Bean에 자동으로 등록 되어 있는 {@link ObjectMapper}를 편리하게 사용하기 위해서 생성.
 *     객체 생성 및 선언이 필요 없이 JsonHelper.Singleton.getInstance().getObjectMapper()와 같은 형태로 호출해서 사용.
 * </pre>
 */
@Component
public class JsonHelper {
    private final ObjectMapper objectMapper;

    public JsonHelper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ObjectMapper getObjectMapper() {
        return this.objectMapper;
    }

    @Component
    public static class Singleton {
        private static JsonHelper JSON_HELPER;

        Singleton(JsonHelper jsonHelper) {
            JSON_HELPER = jsonHelper;
        }

        public static JsonHelper getInstance() {
            return JSON_HELPER;
        }
    }
}
