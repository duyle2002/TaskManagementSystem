package duy.personalproject.taskmanagementsystem.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Standard API response wrapper for all endpoints.
 * Provides consistent response format across the application.
 *
 * @param <T> the type of data being returned
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    /**
     * Indicates whether the request was successful.
     */
    @Builder.Default
    private boolean success = true;

    /**
     * HTTP status code of the response.
     */
    private int code;

    /**
     * Response message describing the result.
     */
    private String message;

    /**
     * Response data payload.
     */
    private T data;

    /**
     * Error details (only present in error responses).
     */
    private ErrorDetails error;


    // ============== Factory Methods ==============

    /**
     * Create a successful response with data.
     */
    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .code(200)
                .message("Success")
                .data(data)
                .build();
    }

    /**
     * Create a successful response with custom message and data.
     */
    public static <T> ApiResponse<T> ok(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .code(200)
                .message(message)
                .data(data)
                .build();
    }

    /**
     * Create a successful response with only message (no data).
     */
    public static <T> ApiResponse<T> okWithMessage(String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .code(200)
                .message(message)
                .build();
    }

    /**
     * Create a created (201) response.
     */
    public static <T> ApiResponse<T> created(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .code(201)
                .message("Resource created successfully")
                .data(data)
                .build();
    }

    /**
     * Create a created (201) response with custom message.
     */
    public static <T> ApiResponse<T> created(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .code(201)
                .message(message)
                .data(data)
                .build();
    }

    /**
     * Create an error response.
     */
    public static <T> ApiResponse<T> error(int code, String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .code(code)
                .message(message)
                .build();
    }

    /**
     * Create an error response with error details.
     */
    public static <T> ApiResponse<T> error(int code, String message, String errorCode, String details) {
        return ApiResponse.<T>builder()
                .success(false)
                .code(code)
                .message(message)
                .error(ErrorDetails.builder()
                        .code(errorCode)
                        .details(details)
                        .build())
                .build();
    }

    /**
     * Create a bad request (400) error response.
     */
    public static <T> ApiResponse<T> badRequest(String message) {
        return error(400, message);
    }

    /**
     * Create an unauthorized (401) error response.
     */
    public static <T> ApiResponse<T> unauthorized(String message) {
        return error(401, message);
    }

    /**
     * Create a forbidden (403) error response.
     */
    public static <T> ApiResponse<T> forbidden(String message) {
        return error(403, message);
    }

    /**
     * Create a not found (404) error response.
     */
    public static <T> ApiResponse<T> notFound(String message) {
        return error(404, message);
    }

    /**
     * Create a conflict (409) error response.
     */
    public static <T> ApiResponse<T> conflict(String message) {
        return error(409, message);
    }

    /**
     * Create an internal server error (500) response.
     */
    public static <T> ApiResponse<T> internalServerError(String message) {
        return error(500, message);
    }
}
