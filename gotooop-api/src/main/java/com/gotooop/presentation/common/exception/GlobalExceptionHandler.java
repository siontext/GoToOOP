package com.gotooop.presentation.common.exception;

import com.gotooop.exception.ApplicationException;
import com.gotooop.presentation.common.response.ErrorResponse;
import com.gotooop.presentation.common.response.ErrorResponse.CustomFieldError;
import com.gotooop.presentation.common.response.code.HttpErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

/**
 * 전역 예외 처리 핸들러.
 *
 * <p>설계 원칙:
 * - import 목록에 gotooop-domain 패키지(com.gotooop.exception.BusinessException,
 *   com.gotooop.common.code.ErrorCode)가 존재하지 않아야 한다. 이것이 레이어 분리의 검증 기준이다.
 * - ApplicationException은 원시 타입(String, int)만 노출하므로 domain 타입에 의존하지 않는다.
 * - Spring MVC 예외는 HttpErrorCode(api 내부) 상수를 사용하여 Magic String을 제거한다.
 * - 시스템 예외(Exception.class)는 반드시 로깅한다. 클라이언트에는 내부 정보를 노출하지 않는다.</p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 비즈니스 예외 처리.
     * Application 레이어에서 domain 예외를 재포장한 ApplicationException을 처리한다.
     */
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException ex, HttpServletRequest request) {
        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(ErrorResponse.of(
                        ex.getErrorCode(),
                        ex.getMessage(),
                        request.getRequestURI()));
    }

    /**
     * 유효성 검사 예외 처리.
     * @Valid 어노테이션으로 인한 MethodArgumentNotValidException을 처리한다.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<CustomFieldError> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new CustomFieldError(
                        fe.getField(),
                        String.valueOf(fe.getRejectedValue()),
                        fe.getDefaultMessage()))
                .toList();
        return ResponseEntity
                .status(HttpErrorCode.BAD_REQUEST.getHttpStatus())
                .body(ErrorResponse.of(HttpErrorCode.BAD_REQUEST, request.getRequestURI(), errors));
    }

    /**
     * 존재하지 않는 URL 요청 처리.
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResource(NoResourceFoundException ex, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpErrorCode.NOT_FOUND.getHttpStatus())
                .body(ErrorResponse.of(HttpErrorCode.NOT_FOUND, request.getRequestURI()));
    }

    /**
     * 미지원 HTTP 메서드 요청 처리.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpErrorCode.METHOD_NOT_ALLOWED.getHttpStatus())
                .body(ErrorResponse.of(HttpErrorCode.METHOD_NOT_ALLOWED, request.getRequestURI()));
    }

    /**
     * 시스템 예외 처리 — 최후 방어선.
     * 예상치 못한 모든 예외를 처리하며, 내부 오류 정보를 클라이언트에 노출하지 않는다.
     * 반드시 서버 로그에 스택트레이스를 기록하여 운영 추적이 가능하도록 한다.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            Exception ex, HttpServletRequest request) {
        log.error("[SystemException] {} {}", request.getMethod(), request.getRequestURI(), ex);
        return ResponseEntity
                .status(HttpErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus())
                .body(ErrorResponse.of(HttpErrorCode.INTERNAL_SERVER_ERROR, request.getRequestURI()));
    }
}
