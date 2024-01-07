package com.notice.common.http;

/**
 * <pre>
 *     Rest 응답코드를 관리하는 인터페이스
 *     분산될 여지가 있는 응답코드들을 정보들을 응집도를 높히기 위해 해당 인터페이스에서 저장.
 * </pre>
 */
public interface ProcessCode {

    String getCode();

    /**
     * <pre>
     *     Rest 전역 응답코드
     * </pre>
     */
    enum Common implements ProcessCode {

        SUCCESS("1000")
        , HTTP_STATUS_EXCEPTION("-9999");

        private final String code;

        Common(String code) {
            this.code = code;
        }

        @Override
        public String getCode() {
            return this.code;
        }
    }

    /**
     * <pre>
     *     Rest Notice 관련 응답코드
     * </pre>
     */
    enum NoticeCode implements ProcessCode {

        BLANK("-1000")
        , NOTICE_DOES_NOT_EXIST("-1001")
        , FILE_DELETE_ERROR("-1002")
        , FILE_UPLOAD_ERROR("-1003");

        private final String code;

        NoticeCode(String code) {
            this.code = code;
        }

        @Override
        public String getCode() {
            return this.code;
        }
    }
}
