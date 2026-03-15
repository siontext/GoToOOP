package com.gotooop.presentation.common.response.code;

/**
 * 실패(에러) 응답 상태를 관리하는 Enum.
 * 
 * <p>비즈니스 해상도를 높이기 위해 구체적인 에러 코드를 제공합니다.</p>
 */
public enum ErrorCode implements ResultCode {
    // 공통 에러 (Common)
    BAD_REQUEST("ERR-COM-400", "잘못된 요청입니다."),
    NOT_FOUND("ERR-COM-404", "요청한 리소스를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR("ERR-COM-500", "서버 내부 오류가 발생했습니다.");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
