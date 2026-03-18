package com.gotooop.common.code;

/**
 * 도메인 비즈니스 오류 코드.
 *
 * <p>설계 원칙:
 * - ResultCode(gotooop-api)를 구현하지 않는다. domain → api 역방향 의존이 발생하기 때문이다.
 * - httpStatus는 int 타입으로 선언한다. Spring HttpStatus enum에 의존하면 domain이 프레임워크에 종속된다.
 * - 도메인별 에러코드는 해당 기능 구현 주차에 추가한다 (YAGNI).</p>
 */
public enum ErrorCode {

    // 공통 에러 (Common)
    BAD_REQUEST("ERR-COM-400", 400, "잘못된 요청입니다."),
    NOT_FOUND("ERR-COM-404", 404, "요청한 리소스를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR("ERR-COM-500", 500, "서버 내부 오류가 발생했습니다.");

    private final String code;
    private final int httpStatus;
    private final String message;

    ErrorCode(String code, int httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public String getCode()    { return code; }
    public int getHttpStatus() { return httpStatus; }
    public String getMessage() { return message; }
}

