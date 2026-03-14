package com.gotooop.presentation.common.response.code;

/**
 * 성공 응답 상태를 관리하는 Enum.
 */
public enum SuccessCode implements ResultCode {
    OK("SUCCESS-200", "요청이 성공적으로 처리되었습니다."),
    CREATED("SUCCESS-201", "리소스가 성공적으로 생성되었습니다.");

    private final String code;
    private final String message;

    SuccessCode(String code, String message) {
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
