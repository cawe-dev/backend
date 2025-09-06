package org.cawe.dev.backend.infrastructure.adapter.rest.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.cawe.dev.backend.domain.exception.EmailAlreadyUsedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.jackson.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("unused")
class ExceptionTranslatorTest {

    private MockMvc mockMvc;

    // Controller to generate the exceptions we want to test.
    @RestController
    static class TestController {
        @GetMapping("/test/email-already-used")
        public void throwEmailAlreadyUsed() {
            throw new EmailAlreadyUsedException();
        }

        @GetMapping("/test/concurrency-failure")
        public void throwConcurrencyFailure() {
            throw new ConcurrencyFailureException("Test concurrency failure");
        }

        @PostMapping("/test/method-argument-not-valid")
        public void throwMethodArgumentNotValid(@Valid @RequestBody TestDTO dto) {
            // This method's body is intentionally empty. Its purpose is to provide a target
            // for Spring's @Valid mechanism. The MethodArgumentNotValidException is thrown
            // by the framework before the method body is ever invoked.
        }

        @GetMapping("/test/http-message-conversion")
        public void throwHttpMessageConversionException() {
            throw new HttpMessageConversionException("Test message conversion error");
        }

        @GetMapping("/test/data-access")
        public void throwDataAccessException() {
            throw new DataAccessResourceFailureException("Test data access error");
        }

        @GetMapping("/test/exception-with-package-name")
        public void throwExceptionWithPackageName() {
            throw new RuntimeException("Error in org.cawe.dev.SomeClass");
        }

        @GetMapping("/test/generic-exception")
        public void throwGenericException() {
            throw new RuntimeException("A detailed error message for debugging");
        }
    }

    // Simple DTO to use in validation testing.
    @Setter
    @Getter
    static class TestDTO {
        @NotEmpty
        private String field;
    }

    @BeforeEach
    void setup() {
        MockEnvironment mockEnvironment = new MockEnvironment();
        mockEnvironment.setProperty("app.name", "test-app");

        ExceptionTranslator exceptionTranslator = new ExceptionTranslator(mockEnvironment);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModules(new ProblemModule(), new ConstraintViolationProblemModule());
        MappingJackson2HttpMessageConverter httpMessageConverter = new MappingJackson2HttpMessageConverter(objectMapper);

        mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
                .setControllerAdvice(exceptionTranslator)
                .setMessageConverters(httpMessageConverter)
                .build();
    }

    @Test
    @DisplayName("Should handle EmailAlreadyUsedException and return 409 Conflict")
    void testHandleEmailAlreadyUsedException() throws Exception {
        mockMvc.perform(get("/test/email-already-used"))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Email is already in use!"))
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    @DisplayName("Should handle ConcurrencyFailureException and return 409 Conflict")
    void testHandleConcurrencyFailureException() throws Exception {
        mockMvc.perform(get("/test/concurrency-failure"))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.message").value("error.concurrencyFailure"))
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException and return 400 Bad Request")
    void testHandleMethodArgumentNotValid() throws Exception {
        TestDTO invalidDto = new TestDTO();
        invalidDto.setField("");

        mockMvc.perform(post("/test/method-argument-not-valid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Method argument not valid"))
                .andExpect(jsonPath("$.fieldErrors[0].objectName").value("test"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("field"))
                .andExpect(jsonPath("$.fieldErrors[0].message").value("must not be empty"));
    }

    @Test
    @DisplayName("Should show detailed message for generic exception in non-prod profile")
    void testPrepareWithGenericExceptionInDevProfile() throws Exception {
        mockMvc.perform(get("/test/generic-exception"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Internal Server Error"))
                .andExpect(jsonPath("$.detail").value("A detailed error message for debugging"));
    }

    @Nested
    @ActiveProfiles("prod")
    @DisplayName("When profile is 'prod'")
    class ProdProfileTests {
        @BeforeEach
        void setupProd() {
            MockEnvironment mockEnvironment = new MockEnvironment();
            mockEnvironment.setActiveProfiles("prod");
            ExceptionTranslator exceptionTranslator = new ExceptionTranslator(mockEnvironment);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModules(new ProblemModule(), new ConstraintViolationProblemModule());
            MappingJackson2HttpMessageConverter httpMessageConverter = new MappingJackson2HttpMessageConverter(objectMapper);
            mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
                    .setControllerAdvice(exceptionTranslator)
                    .setMessageConverters(httpMessageConverter)
                    .build();
        }

        @Test
        @DisplayName("Should hide details for HttpMessageConversionException")
        void testPrepareWithHttpMessageConversionExceptionInProd() throws Exception {
            mockMvc.perform(get("/test/http-message-conversion"))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.detail").value("Unable to convert http message"));
        }

        @Test
        @DisplayName("Should hide details for DataAccessException")
        void testPrepareWithDataAccessExceptionInProd() throws Exception {
            mockMvc.perform(get("/test/data-access"))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.detail").value("Failure during data access"));
        }

        @Test
        @DisplayName("Should hide details for exception messages containing package names")
        void testPrepareWithPackageNameInExceptionMessageInProd() throws Exception {
            mockMvc.perform(get("/test/exception-with-package-name"))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.detail").value("Unexpected runtime exception"));
        }
    }
}