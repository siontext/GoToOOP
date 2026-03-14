package com.gotooop.presentation.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gotooop.presentation.common.response.code.ErrorCode;
import java.util.Collections;
import java.util.List;

/**
 * 실패 응답 전용 레코드.
 * 
 * <p>설계 원칙: 책임의 분리(SRP), 프레임워크 독립성.
 * 상세 비즈니스 코드와 메시지, 그리고 요청 경로(path)와 유효성 검사 내역(errors)을 포함합니다.</p>
 */
public record ErrorResponse(
    String errorCode,
    String message,
    String path,
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<CustomFieldError> errors
) {
    /**
     * 일반적인 비즈니스 에러용 팩토리 메서드. (일반 비즈니스 로직 예외 처리)
     */
    public static ErrorResponse of(ErrorCode errorCode, String path) {
        return new ErrorResponse(errorCode.getCode(), errorCode.getMessage(), path, Collections.emptyList());
    }

    /**
     * 유효성 검사 에러 전용 팩토리 메서드. (유효성 검사시 예외가 발생했을때 사용)
     */
    public static ErrorResponse of(ErrorCode errorCode, String path, List<CustomFieldError> errors) {
        return new ErrorResponse(errorCode.getCode(), errorCode.getMessage(), path, errors);
    }

    /**
     * Spring 프레임워크와의 결합도를 낮추기 위한 우리만의 DTO.
     * 필드 유효성 검사 에러를 캡슐화합니다.
     */
    public record CustomFieldError(
        String field,
        String rejectedValue,
        String reason
    ) {
        // Spring의 org.springframework.validation.FieldError를 우리만의 DTO로 변환하는 of() 메서드는 
        // 컨트롤러 어드바이스 단계에서 구현 예정 (의존성 단절 유지)
    }
}
