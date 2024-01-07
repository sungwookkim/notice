package com.notice.spring.config.exception;

import com.notice.common.http.ProcessCode;
import com.notice.common.http.RestResponseResult;
import com.notice.exception.ProcessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * <pre>
 *     Rest와 관련된 모든 예외를 전역으로 처리.
 * </pre>
 */
@RestControllerAdvice
@EnableWebMvc
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * <pre>
     *     프로세스 처리 예외인 {@link ProcessException} 예외에 대한 응답 값이 설정하여 응답 반환.
     * </pre>
     *
     * @param processException
     * @return
     */
    @ExceptionHandler(ProcessException.class)
    public ResponseEntity<RestResponseResult<Object>> processException(ProcessException processException) {

        return ResponseEntity.ok(new RestResponseResult<>(processException.getProcessCode()
                , ""
                , HttpStatus.SERVICE_UNAVAILABLE));
    }

    /**
     * <pre>
     *     예외에 따른 응답 값을 공통으로 처리하는 부분을 현 프로젝트에 맞게 끔 반환하게 하기 위해 오버라이드.
     * </pre>
     *
     * @param ex the exception to handle
     * @param body the body to use for the response
     * @param headers the headers to use for the response
     * @param statusCode the status code to use for the response
     * @param request the current request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex
            , Object body
            , HttpHeaders headers
            , HttpStatusCode statusCode
            , WebRequest request) {

        ex.printStackTrace();

        return ResponseEntity.ok(new RestResponseResult<>(ProcessCode.Common.HTTP_STATUS_EXCEPTION
                , ""
                , HttpStatus.resolve(statusCode.value())));
    }
}
