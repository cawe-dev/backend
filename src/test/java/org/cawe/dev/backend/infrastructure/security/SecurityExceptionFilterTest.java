package org.cawe.dev.backend.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.cawe.dev.backend.infrastructure.adapter.rest.exception.ProblemVM;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityExceptionFilterTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private PrintWriter printWriter;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private SecurityExceptionFilter securityExceptionFilter;

    @Test
    @DisplayName("It should call the next filter in the chain when no exception occurs")
    void testDoFilterInternalWhenNoException() throws Exception {
        securityExceptionFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(response, never()).setStatus(anyInt());
        verify(response, never()).setContentType(anyString());
        verify(printWriter, never()).write(anyString());
    }

    @Test
    @DisplayName("It should handle exceptions and write a 500 error response")
    void testDoFilterInternalWhenExceptionIsThrown() throws Exception {
        RuntimeException exception = new RuntimeException("Unexpected error");
        doThrow(exception).when(filterChain).doFilter(request, response);
        when(response.getWriter()).thenReturn(printWriter);


        securityExceptionFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);

        ArgumentCaptor<ProblemVM> problemVMCaptor = ArgumentCaptor.forClass(ProblemVM.class);
        verify(objectMapper).writeValueAsString(problemVMCaptor.capture());

        ProblemVM capturedProblem = problemVMCaptor.getValue();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), capturedProblem.title());
        assertEquals("Unexpected error", capturedProblem.detail());

        String expectedJson = objectMapper.writeValueAsString(capturedProblem);
        verify(printWriter).write(expectedJson);
    }
}

