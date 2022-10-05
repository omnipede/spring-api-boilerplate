package io.omnipede.boilerplate.system.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;

@Slf4j
@ControllerAdvice
class GlobalExceptionHandler {

    /**
     *  javax.validation.Valid or @Validated 으로 binding error 발생시 발생한다.
     *  HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할경우 발생
     *  주로 @RequestBody, @RequestPart 어노테이션에서 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<RestError> handleMethodArgumentNotValidException (final MethodArgumentNotValidException e) {
        ErrorCode errorCode = ErrorCode.BAD_REQUEST;
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = errorCode.getMessage();
        if (fieldError != null)
            message = fieldError.getField() + ": " + fieldError.getDefaultMessage();

        return createResponseEntityAndLogError(errorCode, message, e);
    }

    /**
     * Controller 의 path variable 또는 query parameter validation 에러가 발생하는 경우
     */
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<RestError> handleConstraintViolationException(final ConstraintViolationException e) {
        ErrorCode errorCode = ErrorCode.BAD_REQUEST;
        return createResponseEntityAndLogError(errorCode,e.getMessage(), e);
    }

    /**
     * Path variable type 이 불일치 하는 경우.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<RestError> handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        ErrorCode errorCode = ErrorCode.BAD_REQUEST;
        return createResponseEntityAndLogError(errorCode, e.getMessage(), e);
    }

    /**
     * 주로 @RequestParam 이 누락될 경우 발생
     * 필요한 query 파라미터 등이 누락될 경우 발생
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<RestError> handleMissingServletRequestParameterException (final MissingServletRequestParameterException e) {
        ErrorCode errorCode = ErrorCode.BAD_REQUEST;
        String message = "Query error: " + e.getParameterName() + ": " + e.getMessage();
        return createResponseEntityAndLogError(errorCode, message, e);
    }

    /**
     * Invalid 한 JSON 을 body 로 전달한 경우
     * {
     *     "name": "foo bar",  <-- 마지막에 ',' 로 끝남
     * }
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<RestError> handleHttpMessageNotReadableException (final HttpMessageNotReadableException e) {
        ErrorCode errorCode = ErrorCode.BAD_REQUEST;
        String message = "Can't read http message ... Please check your request format.";
        return createResponseEntityAndLogError(errorCode, message, e);
    }

    /**
     * API end point 가 존재하지 않는 경우
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<RestError> handleNoHandlerFoundException(final NoHandlerFoundException e) {
        ErrorCode errorCode = ErrorCode.NOT_FOUND;
        String message = "Maybe you requested to wrong uri";
        return createResponseEntityAndLogError(errorCode, message, e);
    }

    /**
     * 지원하지 않는 http method 요청시 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<RestError> handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException e) {
        ErrorCode errorCode = ErrorCode.NOT_FOUND;
        String message = "Maybe you're requesting not supported http method";
        return createResponseEntityAndLogError(errorCode, message, e);
    }

    /**
     * 지원하지 않는 content type 요청시 발생
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    protected ResponseEntity<RestError> handleHttpMediaTypeNotSupportedException(final HttpMediaTypeNotSupportedException e) {
        ErrorCode errorCode = ErrorCode.BAD_REQUEST;
        String message = e.getMessage();
        return createResponseEntityAndLogError(errorCode, message, e);
    }

    /**
     * 서버에서 정의한 system exception 처리
     */
    @ExceptionHandler(SystemException.class)
    protected ResponseEntity<RestError> handleSystemException(final SystemException e) {
        return createResponseEntityAndLogError(e.getErrorCode(), e.getMessage(), e);
    }

    /**
     * 그 외 서버 에러 발생 시
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<RestError> handleException(final Exception e) {
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        String message = e.getMessage();
        return createResponseEntityAndLogError(errorCode, message, e);
    }

    /**
     * Http response 객체를 생성하고 에러를 로깅하는 메소드
     * @param errorCode AA 서버에서 정의한 에러 타입
     * @param detailedMessage 상세 에러 메시
     * @param e 에러 객체
     * @return http response
     */
    private ResponseEntity<RestError> createResponseEntityAndLogError(ErrorCode errorCode, String detailedMessage, Throwable e) {
        logError(e, errorCode);
        RestError restError = new RestError(errorCode.getCode(), errorCode.getMessage(), detailedMessage);
        return new ResponseEntity<>(restError, HttpStatus.valueOf(errorCode.getStatus()));
    }

    /**
     * 에러 로깅 메소드
     * @param e 로깅할 에러
     * @param errorCode 에러 enum
     */
    private void logError(Throwable e, ErrorCode errorCode) {
        // 서버 에러일경우 => Stack trace 로깅
        if (errorCode == ErrorCode.INTERNAL_SERVER_ERROR)
            log.error("Stack trace: ", e);
        // Debug mode => Stack trace 로깅
        else if (log.isDebugEnabled())
            log.error("Stack trace: ", e);
        // 이외에는 간략하게 에러 메시지만 로깅한다
        else
            log.error(e.getMessage());
    }
}
