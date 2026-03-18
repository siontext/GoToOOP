package com.gotooop.exception;

import com.gotooop.common.code.ErrorCode;

/**
 * Application 레이어의 예외.
 *
 * <p>설계 원칙:
 * - Domain의 BusinessException을 원시 타입(String, int)으로 변환하여 보유한다.
 * - Presentation 레이어는 이 클래스만 알면 되며, domain 타입(ErrorCode)을 전혀 알 필요가 없다.
 * - 생성자는 private으로 선언하여 from() 또는 of() 팩토리 메서드를 통해서만 생성하도록 강제한다.</p>
 *
 * <p>팩토리 메서드 사용 기준:
 * - from(BusinessException) : Domain에서 발생한 예외를 Application으로 전환할 때
 * - of(ErrorCode)           : Domain의 에러코드를 재사용하여 Application에서 직접 예외를 생성할 때
 * - of(String, int, String) : 테스트 등 특수 목적으로 원시 타입을 직접 지정해야 할 때 (최후 수단)</p>
 */
public class ApplicationException extends RuntimeException {

    private final String errorCode;  // domain 타입 아님 — String
    private final int httpStatus;    // domain 타입 아님 — int

    private ApplicationException(String errorCode, int httpStatus, String message) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    /**
     * Domain의 BusinessException을 ApplicationException으로 변환한다.
     * 메서드 체이닝으로 ErrorCode를 이름으로 참조하지 않아 불필요한 의존을 최소화한다.
     */
    public static ApplicationException from(BusinessException e) {
        return new ApplicationException(
            e.getErrorCode().getCode(),
            e.getErrorCode().getHttpStatus(),
            e.getErrorCode().getMessage()
        );
    }

    /**
     * Domain의 ErrorCode를 사용하여 ApplicationException을 생성한다.
     * Application 레이어 자체 검증 실패 시 Magic String 없이 타입 안전하게 예외를 생성한다.
     * Application → Domain 의존은 허용된 방향이므로 ErrorCode를 직접 받는다.
     */
    public static ApplicationException of(ErrorCode code) {
        return new ApplicationException(
            code.getCode(),
            code.getHttpStatus(),
            code.getMessage()
        );
    }

    /**
     * 원시 타입으로 직접 ApplicationException을 생성한다.
     * ErrorCode에 정의되지 않은 케이스나 테스트 목적으로만 사용한다 (최후 수단).
     */
    public static ApplicationException of(String errorCode, int httpStatus, String message) {
        return new ApplicationException(errorCode, httpStatus, message);
    }

    public String getErrorCode() { return errorCode; }  // 반환 타입: String (domain 타입 아님)
    public int getHttpStatus()   { return httpStatus; } // 반환 타입: int   (domain 타입 아님)
}

