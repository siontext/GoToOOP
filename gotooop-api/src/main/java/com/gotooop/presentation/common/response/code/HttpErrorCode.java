package com.gotooop.presentation.common.response.code;

/**
 * Presentation 레이어 전용 HTTP 에러 상수.
 *
 * <p>설계 원칙:
 * - GlobalExceptionHandler에서 Spring MVC 예외(유효성 검사, 잘못된 URL 등)를 처리할 때 사용한다.
 * - domain 타입(ErrorCode)과 완전히 독립적이며, Presentation 레이어가 자체적으로 관리한다.
 * - ResultCode 인터페이스를 구현하여 SuccessCode와 동일한 다형성 계층에 속한다.
 * - httpStatus를 직접 보유하여 GlobalExceptionHandler가 매직 넘버 없이 상태 코드를 결정한다.</p>
 *
 * <p>⚠️ ErrorCode(domain)와 코드값이 동일한 것은 의도적 중복이다.
 * 레이어 분리를 지키기 위해 각 레이어가 독립적으로 상수를 관리하며,
 * ErrorCode 값이 바뀌면 이 클래스도 함께 수정해야 한다.</p>
 */
public enum HttpErrorCode implements ResultCode {

    BAD_REQUEST(400,  "ERR-COM-400", "잘못된 요청입니다."),
    NOT_FOUND(404,    "ERR-COM-404", "요청한 리소스를 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED(405, "ERR-COM-405", "지원하지 않는 HTTP 메서드입니다."),
    INTERNAL_SERVER_ERROR(500, "ERR-COM-500", "서버 내부 오류가 발생했습니다.");

    private final int httpStatus;
    private final String code;
    private final String message;

    HttpErrorCode(int httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    public int getHttpStatus() { return httpStatus; }

    @Override
    public String getCode()    { return code; }

    @Override
    public String getMessage() { return message; }
}

