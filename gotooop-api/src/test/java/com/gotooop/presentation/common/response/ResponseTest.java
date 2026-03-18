package com.gotooop.presentation.common.response;

import com.gotooop.presentation.common.response.code.HttpErrorCode;
import com.gotooop.presentation.common.response.code.ResultCode;
import com.gotooop.presentation.common.response.code.SuccessCode;
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
        String errorCode = HttpErrorCode.NOT_FOUND.getCode();
        String message   = HttpErrorCode.NOT_FOUND.getMessage();
        String path = "/api/v1/posts/999";
        List<ErrorResponse.CustomFieldError> errors = List.of(
            new ErrorResponse.CustomFieldError("id", "999", "존재하지 않는 리소스입니다.")
        );

        // when
        ErrorResponse response = ErrorResponse.of(errorCode, message, path, errors);

        // then
        assertEquals(errorCode, response.errorCode());
        assertEquals(message, response.message());
        assertEquals(path, response.path());
        assertEquals(1, response.errors().size());
        assertEquals("id", response.errors().get(0).field());
    }

    @Test
    @DisplayName("필드 에러 없는 ErrorResponse 생성 시 errors는 빈 리스트여야 한다")
    void errorResponse_WithoutErrors_HasEmptyList() {
        // given
        String errorCode = HttpErrorCode.BAD_REQUEST.getCode();
        String message   = HttpErrorCode.BAD_REQUEST.getMessage();
        String path = "/api/v1/posts";

        // when
        ErrorResponse response = ErrorResponse.of(errorCode, message, path);

        // then
        assertEquals(errorCode, response.errorCode());
        assertEquals(message, response.message());
        assertEquals(path, response.path());
        assertTrue(response.errors().isEmpty());
    }

    @Test
    @DisplayName("SuccessCode는 올바른 코드값과 메시지를 반환해야 한다")
    void successCode_ReturnsCorrectCodeAndMessage() {
        // ResultCode 인터페이스 다형성 검증
        ResultCode ok = SuccessCode.OK;
        ResultCode created = SuccessCode.CREATED;

        assertEquals("SUCCESS-200", ok.getCode());
        assertEquals("요청이 성공적으로 처리되었습니다.", ok.getMessage());
        assertEquals("SUCCESS-201", created.getCode());
        assertEquals("리소스가 성공적으로 생성되었습니다.", created.getMessage());
    }

    @Test
    @DisplayName("레코드(Record)는 기본적으로 불변성을 유지해야 한다")
    void record_IsImmutable() {
        // Record는 모든 필드가 private final이며 런타임에 수정 불가
        assertTrue(SuccessResponse.class.isRecord());
        assertTrue(ErrorResponse.class.isRecord());
    }
}
