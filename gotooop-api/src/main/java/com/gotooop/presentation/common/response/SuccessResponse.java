package com.gotooop.presentation.common.response;

import com.gotooop.presentation.common.response.code.SuccessCode;

/**
 * 성공 응답 전용 레코드.
 * 
 * <p>설계 원칙: 불변성(Immutable), 응집도 극대화.
 * 성공 시 반환할 데이터(data)만 보유하며, 에러와 관련된 불필요한 필드는 존재하지 않습니다.</p>
 */
public record SuccessResponse<T>(T data) {
    
    /**
     * 기본 성공 데이터와 함께 응답을 생성합니다.
     */
    public static <T> SuccessResponse<T> ok(T data) {
        return new SuccessResponse<>(data);
    }
}
