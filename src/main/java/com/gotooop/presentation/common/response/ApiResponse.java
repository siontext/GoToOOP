package com.gotooop.presentation.common.response;

import java.time.LocalDateTime;

/**
 * API 공통 응답 포맷 클래스.
 * 
 * <p>설계 원칙: Lombok 미사용, 불변성 유지, 제네릭 활용, 정적 팩토리 메서드 제공.</p>
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
     * private 생성자를 통해 외부에서 직접 생성을 제한하고 정적 팩토리 메서드 사용을 유도합니다.
     */
    private ApiResponse(boolean success, int code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
        this.serverTime = LocalDateTime.now();
    }

    /**
     * 성공 응답을 생성합니다. (메시지 기본값)
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, 200, "요청이 성공적으로 처리되었습니다.", data);
    }

    /**
     * 성공 응답을 생성합니다. (메시지 커스텀)
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, 200, message, data);
    }

    /**
     * 실패 응답을 생성합니다.
     */
    public static <T> ApiResponse<T> fail(int code, String message) {
        return new ApiResponse<>(false, code, message, null);
    }

    // Getters (Lombok을 사용하지 않으므로 직접 작성)
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
