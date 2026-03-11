package com.gotooop.presentation.common.response.code;

/**
 * 성공 응답 상태를 관리하는 Enum.
 */
public enum SuccessCode implements ResultCode {
    OK(200, "요청이 성공적으로 처리되었습니다."),
    CREATED(201, "리소스가 성공적으로 생성되었습니다.");

    private final int code;
    private final String message;

    SuccessCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
