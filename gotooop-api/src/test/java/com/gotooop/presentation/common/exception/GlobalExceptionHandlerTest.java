package com.gotooop.presentation.common.exception;

import com.gotooop.exception.ApplicationException;
import com.gotooop.presentation.common.response.code.HttpErrorCode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = GlobalExceptionHandlerTest.TestController.class)
@DisplayName("GlobalExceptionHandler 통합 테스트")
class GlobalExceptionHandlerTest {

    @Autowired
    MockMvc mockMvc;

    // ── 테스트용 컨트롤러 ──────────────────────────────────────────────────
    @RestController
    @RequestMapping("/test")
    static class TestController {

        @GetMapping("/app-exception")
        void throwApplicationException() {
            throw ApplicationException.of("ERR-TEST-001", 404, "테스트 리소스를 찾을 수 없습니다.");
        }

        @PostMapping("/validation")
        void validateRequest(@Valid @RequestBody TestRequest request) {}

        @GetMapping("/system-exception")
        void throwSystemException() {
            throw new RuntimeException("예상치 못한 시스템 오류");
        }
    }

    record TestRequest(@NotBlank(message = "이름은 필수입니다.") String name) {}

    // ──────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("비즈니스 예외 처리")
    class BusinessExceptionHandling {

        @Test
        @DisplayName("ApplicationException 발생 시 지정된 HTTP 상태코드와 에러 응답을 반환한다")
        void applicationException_ReturnsCorrectStatusAndBody() throws Exception {
            mockMvc.perform(get("/test/app-exception"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.errorCode").value("ERR-TEST-001"))
                    .andExpect(jsonPath("$.message").value("테스트 리소스를 찾을 수 없습니다."))
                    .andExpect(jsonPath("$.path").value("/test/app-exception"))
                    .andExpect(jsonPath("$.errors").doesNotExist()); // NON_EMPTY 설정으로 빈 배열은 직렬화 제외
        }
    }

    @Nested
    @DisplayName("유효성 검사 예외 처리")
    class ValidationExceptionHandling {

        @Test
        @DisplayName("@Valid 검사 실패 시 400과 필드 에러 목록을 반환한다")
        void validationException_Returns400WithFieldErrors() throws Exception {
            mockMvc.perform(post("/test/validation")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\": \"\"}"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorCode").value(HttpErrorCode.BAD_REQUEST.getCode()))
                    .andExpect(jsonPath("$.errors").isNotEmpty())
                    .andExpect(jsonPath("$.errors[0].field").value("name"))
                    .andExpect(jsonPath("$.errors[0].reason").value("이름은 필수입니다."));
        }
    }

    @Nested
    @DisplayName("Spring MVC 예외 처리")
    class SpringMvcExceptionHandling {

        @Test
        @DisplayName("존재하지 않는 URL 요청 시 404와 에러 응답을 반환한다")
        void notFoundUrl_Returns404() throws Exception {
            mockMvc.perform(get("/non-existent-path"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.errorCode").value(HttpErrorCode.NOT_FOUND.getCode()))
                    .andExpect(jsonPath("$.message").value(HttpErrorCode.NOT_FOUND.getMessage()));
        }

        @Test
        @DisplayName("미지원 HTTP 메서드 요청 시 405와 에러 응답을 반환한다")
        void methodNotAllowed_Returns405() throws Exception {
            mockMvc.perform(delete("/test/app-exception"))  // DELETE는 TestController에 미정의
                    .andExpect(status().isMethodNotAllowed())
                    .andExpect(jsonPath("$.errorCode").value(HttpErrorCode.METHOD_NOT_ALLOWED.getCode()));
        }
    }

    @Nested
    @DisplayName("시스템 예외 처리")
    class SystemExceptionHandling {

        @Test
        @DisplayName("예상치 못한 예외 발생 시 500을 반환하고 내부 오류를 노출하지 않는다")
        void unexpectedException_Returns500WithoutInternalDetails() throws Exception {
            mockMvc.perform(get("/test/system-exception"))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.errorCode").value(HttpErrorCode.INTERNAL_SERVER_ERROR.getCode()))
                    .andExpect(jsonPath("$.message").value(HttpErrorCode.INTERNAL_SERVER_ERROR.getMessage()));
        }
    }

    @Nested
    @DisplayName("응답 구조 일관성")
    class ResponseStructureConsistency {

        @Test
        @DisplayName("모든 에러 응답은 errorCode, message, path 필드를 포함한다")
        void allErrorResponses_HaveConsistentStructure() throws Exception {
            mockMvc.perform(get("/test/app-exception"))
                    .andExpect(jsonPath("$.errorCode").exists())
                    .andExpect(jsonPath("$.message").exists())
                    .andExpect(jsonPath("$.path").exists());
        }
    }
}

