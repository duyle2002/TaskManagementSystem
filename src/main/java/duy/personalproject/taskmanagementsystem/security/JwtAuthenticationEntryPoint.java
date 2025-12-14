package duy.personalproject.taskmanagementsystem.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import duy.personalproject.taskmanagementsystem.exception.ErrorCode;
import duy.personalproject.taskmanagementsystem.model.common.ApiResponse;
import duy.personalproject.taskmanagementsystem.model.common.ErrorDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "SECURITY")
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {
        log.error("Unauthorized access attempt: {}", authException.getMessage());

        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .success(false)
                .code(HttpStatus.UNAUTHORIZED.value())
                .message(errorCode.getMessage())
                .error(ErrorDetails.of(errorCode.getCode(), authException.getMessage()))
                .build();

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), apiResponse);
    }
}

