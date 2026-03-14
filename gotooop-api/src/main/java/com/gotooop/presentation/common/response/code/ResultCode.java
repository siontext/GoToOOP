package com.gotooop.presentation.common.response.code;

/**
 * 응답 코드와 메시지를 정의하기 위한 인터페이스.
 * 
 * <p>성공(SuccessCode)과 실패(ErrorCode) 상황을 공통된 타입으로 다루기 위해 사용합니다.
 * 비즈니스 해상도를 높이기 위해 코드는 String 타입을 사용합니다.</p>
 */
public interface ResultCode {
    String getCode();
    String getMessage();
}
