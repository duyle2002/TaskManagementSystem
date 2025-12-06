package duy.personalproject.taskmanagementsystem.exception;

import duy.personalproject.taskmanagementsystem.model.common.ApiResponse;
import duy.personalproject.taskmanagementsystem.model.common.ErrorDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global exception handler for all REST endpoints.
 * Handles various exception types and returns standardized API responses.
 *
 * Best practices:
 * - Centralized exception handling
 * - Consistent error response format
 * - Proper HTTP status codes
 * - Secure error messages (no internal details in production)
 * - Detailed logging for debugging
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(
            BusinessException e, WebRequest request) {
        log.warn("Business exception occurred: {}", e.getErrorCode(), e);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .code(e.getHttpStatusCode())
                .message(e.getMessage())
                .error(ErrorDetails.of(e.getErrorCodeValue(), e.getMessage()))
                .build();

        return new ResponseEntity<>(response, HttpStatus.valueOf(e.getHttpStatusCode()));
    }

    /**
     * Handle validation errors from @Valid annotation.
     * Collects all field validation errors and returns them in the response.
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(
            MethodArgumentNotValidException e, WebRequest request) {
        log.warn("Validation exception occurred");

        Map<String, String> fieldErrors = e.getBindingResult()
                .getAllErrors()
                .stream()
                .collect(Collectors.toMap(
                        error -> ((FieldError) error).getField(),
                        error -> error.getDefaultMessage(),
                        (existing, replacement) -> existing // Keep first error if duplicate
                ));

        ErrorCode errorCode = ErrorCode.VALIDATION_ERROR;
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .code(400)
                .message("Validation failed")
                .error(ErrorDetails.builder()
                        .code(errorCode.getCode())
                        .details("Request validation failed")
                        .fieldErrors(fieldErrors)
                        .build())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle ResourceNotFoundException.
     */
    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(
            ResourceNotFoundException e, WebRequest request) {
        log.warn("Resource not found: {}", e.getMessage());

        ErrorCode errorCode = ErrorCode.RESOURCE_NOT_FOUND;
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .code(404)
                .message(e.getMessage())
                .error(ErrorDetails.of(errorCode.getCode(), e.getMessage()))
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle DuplicateResourceException.
     */
    @ExceptionHandler(value = DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateResourceException(
            DuplicateResourceException e, WebRequest request) {
        log.warn("Duplicate resource detected: {}", e.getMessage());

        ErrorCode errorCode = ErrorCode.DUPLICATE_RESOURCE;
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .code(409)
                .message(e.getMessage())
                .error(ErrorDetails.of(errorCode.getCode(), e.getMessage()))
                .build();

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    /**
     * Handle UnauthorizedException.
     */
    @ExceptionHandler(value = UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnauthorizedException(
            UnauthorizedException e, WebRequest request) {
        log.warn("Unauthorized access attempt: {}", e.getMessage());

        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .code(401)
                .message(e.getMessage())
                .error(ErrorDetails.of(errorCode.getCode(), e.getMessage()))
                .build();

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handle ForbiddenException.
     */
    @ExceptionHandler(value = ForbiddenException.class)
    public ResponseEntity<ApiResponse<Void>> handleForbiddenException(
            ForbiddenException e, WebRequest request) {
        log.warn("Forbidden access attempt: {}", e.getMessage());

        ErrorCode errorCode = ErrorCode.FORBIDDEN;
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .code(403)
                .message(e.getMessage())
                .error(ErrorDetails.of(errorCode.getCode(), e.getMessage()))
                .build();

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    /**
     * Handle 404 - Endpoint not found.
     */
    @ExceptionHandler(value = NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoResourceFoundException(
            NoResourceFoundException e, WebRequest request) {
        log.warn("Endpoint not found: {} {}", e.getHttpMethod(), e.getResourcePath());

        ErrorCode errorCode = ErrorCode.ENDPOINT_NOT_FOUND;
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .code(404)
                .message("Endpoint not found")
                .error(ErrorDetails.of(errorCode.getCode(),
                        String.format("No handler found for %s %s", e.getHttpMethod(), e.getResourcePath())))
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle all other exceptions (generic catch-all).
     * Should be logged for investigation and not expose internal details to clients.
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(
            Exception e, WebRequest request) {
        log.error("Unexpected exception occurred", e);

        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .code(500)
                .message("An unexpected error occurred. Please try again later.")
                .error(ErrorDetails.of(errorCode.getCode(),
                        "If the problem persists, please contact support."))
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
