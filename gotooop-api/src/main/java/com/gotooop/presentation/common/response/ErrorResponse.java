package com.gotooop.presentation.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gotooop.presentation.common.response.code.ResultCode;
import java.util.Collections;
import java.util.List;

/**
 * 실패 응답 전용 레코드.
 *
 * <p>설계 원칙: 책임의 분리(SRP), 프레임워크 독립성.
 * - ResultCode 인터페이스를 받는 팩토리 메서드를 제공하여 구체 타입(HttpErrorCode)에 의존하지 않는다.
 * - String 기반 팩토리 메서드는 ApplicationException(원시 타입) 처리를 위해 유지한다.
 * - ErrorCode(domain 타입)를 직접 받는 메서드는 gotooop-api → gotooop-domain 레이어 침범을 유발하므로 제거한다.</p>
 */
public record ErrorResponse(
    String errorCode,
    String message,
    String path,
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<CustomFieldError> errors
) {
    /**
     * ResultCode 인터페이스 기반 에러 응답 생성.
     * HttpErrorCode 등 ResultCode 구현체를 직접 전달할 수 있다.
     */
    public static ErrorResponse of(ResultCode code, String path) {
        return new ErrorResponse(code.getCode(), code.getMessage(), path, Collections.emptyList());
    }

    /**
     * ResultCode 인터페이스 기반 유효성 검사 에러 응답 생성.
     */
    public static ErrorResponse of(ResultCode code, String path, List<CustomFieldError> errors) {
        return new ErrorResponse(code.getCode(), code.getMessage(), path, errors);
    }

    /**
     * 원시 타입 기반 에러 응답 생성.
     * ApplicationException처럼 이미 원시 타입(String)으로 변환된 값을 처리할 때 사용한다.
     */
    public static ErrorResponse of(String errorCode, String message, String path) {
        return new ErrorResponse(errorCode, message, path, Collections.emptyList());
    }

    /**
     * 원시 타입 기반 유효성 검사 에러 응답 생성.
     */
    public static ErrorResponse of(String errorCode, String message, String path,
                                   List<CustomFieldError> errors) {
        return new ErrorResponse(errorCode, message, path, errors);
    }


    /**
     * Spring 프레임워크와의 결합도를 낮추기 위한 우리만의 DTO.
     * 필드 유효성 검사 에러를 캡슐화한다.
     */
    public record CustomFieldError(
        String field,
        String rejectedValue,
        String reason
    ) {}
}
