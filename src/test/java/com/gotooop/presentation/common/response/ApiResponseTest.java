package com.gotooop.presentation.common.response;

import com.gotooop.presentation.common.response.code.ErrorCode;
import com.gotooop.presentation.common.response.code.SuccessCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ApiResponse 단위 테스트")
class ApiResponseTest {

    @Test
    @DisplayName("성공 응답 생성 시 기본 SuccessCode.OK 상태와 데이터가 정확히 포함되어야 한다")
    void success_WithData_ReturnsCorrectResponse() {
        // given
        String testData = "Hello, World!";

        // when
        ApiResponse<String> response = ApiResponse.success(testData);

        // then
        assertTrue(response.isSuccess());
        assertEquals(SuccessCode.OK.getCode(), response.getCode());
        assertEquals(SuccessCode.OK.getMessage(), response.getMessage());
        assertEquals(testData, response.getData());
        assertNotNull(response.getServerTime());
    }

    @Test
    @DisplayName("성공 응답 생성 시 특정 SuccessCode 상태가 정확히 포함되어야 한다")
    void success_WithDataAndSuccessCode_ReturnsCorrectResponse() {
        // given
        String testData = "Created Data";
        SuccessCode createdCode = SuccessCode.CREATED;

        // when
        ApiResponse<String> response = ApiResponse.success(testData, createdCode);

        // then
        assertTrue(response.isSuccess());
        assertEquals(createdCode.getCode(), response.getCode());
        assertEquals(createdCode.getMessage(), response.getMessage());
        assertEquals(testData, response.getData());
    }

    @Test
    @DisplayName("실패 응답 생성 시 ErrorCode 상태가 정확히 포함되어야 한다")
    void fail_ReturnsCorrectResponse() {
        // given
        ErrorCode errorCode = ErrorCode.BAD_REQUEST;

        // when
        ApiResponse<Void> response = ApiResponse.fail(errorCode);

        // then
        assertFalse(response.isSuccess());
        assertEquals(errorCode.getCode(), response.getCode());
        assertEquals(errorCode.getMessage(), response.getMessage());
        assertNull(response.getData());
    }

    @Test
    @DisplayName("ApiResponse 클래스는 불변성을 유지해야 하며 모든 필드는 final이어야 한다")
    void immutability_CheckAllFieldsAreFinal() {
        Field[] fields = ApiResponse.class.getDeclaredFields();
        for (Field field : fields) {
            // JVM에 의해 자동 생성된 필드($로 시작)는 제외
            if (!field.getName().startsWith("$")) {
                assertTrue(Modifier.isFinal(field.getModifiers()), 
                        "Field '" + field.getName() + "' should be final");
            }
        }
    }

    @Test
    @DisplayName("생성자는 private이어야 하며 정적 팩토리 메서드 사용을 강제해야 한다")
    void constructor_IsPrivate() {
        Constructor<?>[] constructors = ApiResponse.class.getDeclaredConstructors();
        assertEquals(1, constructors.length);
        assertTrue(Modifier.isPrivate(constructors[0].getModifiers()), 
                "Constructor should be private");
    }
}
