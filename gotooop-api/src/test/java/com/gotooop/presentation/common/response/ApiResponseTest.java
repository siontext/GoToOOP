package com.gotooop.presentation.common.response;

import com.gotooop.presentation.common.response.code.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("응답 객체 단위 테스트 (분리형 Record 버전)")
class ResponseTest {

    @Test
    @DisplayName("성공 응답(SuccessResponse)은 오직 데이터만 포함해야 한다")
    void successResponse_OnlyContainsData() {
        // given
        String data = "OK Data";

        // when
        SuccessResponse<String> response = SuccessResponse.ok(data);

        // then
        assertEquals(data, response.data());
    }

    @Test
    @DisplayName("실패 응답(ErrorResponse)은 비즈니스 코드, 메시지, 경로 및 상세 에러를 정확히 포함해야 한다")
    void errorResponse_ContainsAllErrorContext() {
        // given
        ErrorCode errorCode = ErrorCode.INSUFFICIENT_BALANCE;
        String path = "/api/v1/payments";
        List<ErrorResponse.CustomFieldError> errors = List.of(
            new ErrorResponse.CustomFieldError("amount", "1000", "잔액이 부족합니다.")
        );

        // when
        ErrorResponse response = ErrorResponse.of(errorCode, path, errors);

        // then
        assertEquals(errorCode.getCode(), response.errorCode());
        assertEquals(errorCode.getMessage(), response.message());
        assertEquals(path, response.path());
        assertEquals(1, response.errors().size());
        assertEquals("amount", response.errors().get(0).field());
    }

    @Test
    @DisplayName("레코드(Record)는 기본적으로 불변성을 유지해야 한다")
    void record_IsImmutable() {
        // Record는 모든 필드가 private final이며 런타임에 수정 불가
        assertTrue(SuccessResponse.class.isRecord());
        assertTrue(ErrorResponse.class.isRecord());
    }
}
