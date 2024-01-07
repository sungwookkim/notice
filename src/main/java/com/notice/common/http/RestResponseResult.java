package com.notice.common.http;

/**
 * <pre>
 *     Rest 응답을 제어하는 클래스
 * </pre>
 *
 * @param <T> 응답 값
 */
public class RestResponseResult<T> {
    private final HttpStatus httpStatus;
    private final String processCode;
    private final T processValue;

    /**
     * <pre>
     *     해당 생성자를 사용하게 되면 기본으로 성공 응답.
     * </pre>
     *
     * @param processValue 응답 값
     */
    public RestResponseResult(T processValue) {

        this(ProcessCode.Common.SUCCESS, processValue, org.springframework.http.HttpStatus.OK);
    }

    public <R extends ProcessCode> RestResponseResult(R processCode, T result, org.springframework.http.HttpStatus httpStatus) {

        this.processCode = processCode.getCode();
        this.processValue = result;
        this.httpStatus = new HttpStatus(String.valueOf(httpStatus.value())
                , httpStatus.getReasonPhrase());
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getProcessCode() {
        return processCode;
    }

    public T getProcessValue() {
        return processValue;
    }

    /**
     * <pre>
     *     값이 없는 Rest 성공을 반환하는 편의 메서드
     * </pre>
     *
     * @return 빈 값의 Rest 성공 객체
     */
    public static RestResponseResult<String> success() {
        return new RestResponseResult<>("");
    }

    /**
     * <pre>
     *     {@link org.springframework.http.HttpStatus}과 관련된 Http 상태코드를 관리하는 클래스
     * </pre>
     */
    class HttpStatus {
        private final String code;
        private final String reasonPhrase;

        public HttpStatus(String code, String reasonPhrase) {

            this.code = code;
            this.reasonPhrase = reasonPhrase;
        }

        public String getCode() {
            return code;
        }

        public String getReasonPhrase() {
            return reasonPhrase;
        }
    }
}
