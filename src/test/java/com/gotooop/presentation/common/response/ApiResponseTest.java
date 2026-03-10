package com.gotooop.presentation.common.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ApiResponse 단위 테스트")
class ApiResponseTest {

    @Test
    @DisplayName("성공 응답 생성 시 기본 메시지와 데이터가 정확히 포함되어야 한다")
    void success_WithData_ReturnsCorrectResponse() {
        // given
        String testData = "Hello, World!";

        // when
        ApiResponse<String> response = ApiResponse.success(testData);

        // then
        assertTrue(response.isSuccess());
        assertEquals(200, response.getCode());
        assertEquals("요청이 성공적으로 처리되었습니다.", response.getMessage());
        assertEquals(testData, response.getData());
        assertNotNull(response.getServerTime());
    }

    @Test
    @DisplayName("성공 응답 생성 시 커스텀 메시지가 정확히 포함되어야 한다")
    void success_WithDataAndMessage_ReturnsCorrectResponse() {
        // given
        String testData = "Data";
        String customMessage = "Custom Success Message";

        // when
        ApiResponse<String> response = ApiResponse.success(testData, customMessage);

        // then
        assertTrue(response.isSuccess());
        assertEquals(200, response.getCode());
        assertEquals(customMessage, response.getMessage());
        assertEquals(testData, response.getData());
        assertNotNull(response.getServerTime());
    }

    @Test
    @DisplayName("실패 응답 생성 시 에러 코드와 메시지가 정확히 포함되어야 한다")
    void fail_ReturnsCorrectResponse() {
        // given
        int errorCode = 400;
        String errorMessage = "Bad Request";

        // when
        ApiResponse<Void> response = ApiResponse.fail(errorCode, errorMessage);

        // then
        assertFalse(response.isSuccess());
        assertEquals(errorCode, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        assertNotNull(response.getServerTime());
    }

    @Test
    @DisplayName("응답 객체의 모든 필드는 Getter를 통해 접근 가능해야 한다")
    void getters_WorkCorrectly() {
        // given
        ApiResponse<String> response = ApiResponse.success("Test");

        // when & then
        assertDoesNotThrow(response::isSuccess);
        assertDoesNotThrow(response::getCode);
        assertDoesNotThrow(response::getMessage);
        assertDoesNotThrow(response::getData);
        assertDoesNotThrow(response::getServerTime);
    }

    @Test
    @DisplayName("ApiResponse 클래스는 불변성을 유지해야 하며 모든 필드는 final이어야 한다")
    void immutability_CheckAllFieldsAreFinal() {
        Field[] fields = ApiResponse.class.getDeclaredFields();
        for (Field field : fields) {
            assertTrue(Modifier.isFinal(field.getModifiers()), 
                    "Field '" + field.getName() + "' should be final");
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
