package com.gotooop.presentation.common.response;

import com.gotooop.presentation.common.response.code.ErrorCode;
import com.gotooop.presentation.common.response.code.ResultCode;
import com.gotooop.presentation.common.response.code.SuccessCode;
import java.time.LocalDateTime;

/**
 * API 공통 응답 포맷 클래스.
 * 
 * <p>설계 원칙: Lombok 미사용, 불변성 유지, 제네릭 활용, ResultCode 인터페이스를 통한 다형성 지원.</p>
 *
 * @param <T> 응답 데이터의 타입
 */
public final class ApiResponse<T> {

    private final boolean success;
    private final int code;
    private final String message;
    private final T data;
    private final LocalDateTime serverTime;

    /**
     * 내부 생성자를 통해 ResultCode 인터페이스로부터 상태 정보를 주입받습니다.
     */
    private ApiResponse(boolean success, ResultCode resultCode, T data) {
        this.success = success;
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
        this.serverTime = LocalDateTime.now();
    }

    /**
     * 성공 응답을 생성합니다. (SuccessCode.OK 기본 사용)
     */
    public static <T> ApiResponse<T> success(T data) {
        return success(data, SuccessCode.OK);
    }

    /**
     * 성공 응답을 생성합니다. (구체적인 SuccessCode 지정)
     */
    public static <T> ApiResponse<T> success(T data, SuccessCode successCode) {
        return new ApiResponse<>(true, successCode, data);
    }

    /**
     * 실패 응답을 생성합니다. (구체적인 ErrorCode 지정)
     */
    public static <T> ApiResponse<T> fail(ErrorCode errorCode) {
        return new ApiResponse<>(false, errorCode, null);
    }

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public LocalDateTime getServerTime() {
        return serverTime;
    }
}
