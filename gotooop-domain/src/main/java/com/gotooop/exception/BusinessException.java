package com.gotooop.exception;

import com.gotooop.common.code.ErrorCode;

/**
 * 도메인 비즈니스 예외의 최상위 추상 클래스.
 *
 * <p>설계 원칙:
 * - abstract로 선언하여 직접 인스턴스화를 금지한다. 항상 구체적인 의미를 가진 하위 클래스로만 사용한다.
 * - Spring, Servlet 등 프레임워크에 전혀 의존하지 않는 순수 Java 클래스다.
 * - ErrorCode를 보유하며, Application 레이어에서 원시 타입으로 변환된 후 상위로 전달된다.</p>
 */
public abstract class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    protected BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}

