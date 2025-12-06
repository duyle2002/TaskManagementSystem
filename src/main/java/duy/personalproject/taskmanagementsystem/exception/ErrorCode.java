package duy.personalproject.taskmanagementsystem.exception;

import lombok.Getter;

/**
 * Enumeration of all error codes used in the application.
 * Provides type-safe error codes for consistent error handling.
 * Each error code is associated with a default HTTP status code and message.
 */
@Getter
public enum ErrorCode {
    // 400 Bad Request Errors
    VALIDATION_ERROR(400, "VALIDATION_ERROR", "Request validation failed"),
    INVALID_REQUEST(400, "INVALID_REQUEST", "Invalid request"),
    BAD_REQUEST(400, "BAD_REQUEST", "Bad request"),
    BUSINESS_ERROR(400, "BUSINESS_ERROR", "Business logic error"),

    // 401 Unauthorized Errors
    UNAUTHORIZED(401, "UNAUTHORIZED", "User is not authenticated"),
    AUTHENTICATION_FAILED(401, "AUTHENTICATION_FAILED", "Authentication failed"),
    TOKEN_EXPIRED(401, "TOKEN_EXPIRED", "Token has expired"),
    INVALID_TOKEN(401, "INVALID_TOKEN", "Invalid token"),
    INVALID_CREDENTIALS(401, "INVALID_CREDENTIALS", "Invalid username or password"),

    // 403 Forbidden Errors
    FORBIDDEN(403, "FORBIDDEN", "Access denied"),
    INSUFFICIENT_PERMISSIONS(403, "INSUFFICIENT_PERMISSIONS", "Insufficient permissions to perform this action"),
    ACCESS_DENIED(403, "ACCESS_DENIED", "Access denied"),

    // 404 Not Found Errors
    RESOURCE_NOT_FOUND(404, "RESOURCE_NOT_FOUND", "Resource not found"),
    USER_NOT_FOUND(404, "USER_NOT_FOUND", "User not found"),
    TASK_NOT_FOUND(404, "TASK_NOT_FOUND", "Task not found"),
    ENDPOINT_NOT_FOUND(404, "ENDPOINT_NOT_FOUND", "Endpoint not found"),

    // 409 Conflict Errors
    DUPLICATE_RESOURCE(409, "DUPLICATE_RESOURCE", "Resource already exists"),
    CONFLICT(409, "CONFLICT", "Resource conflict"),
    USER_ALREADY_EXISTS(409, "USER_ALREADY_EXISTS", "User already exists"),
    EMAIL_ALREADY_EXISTS(409, "EMAIL_ALREADY_EXISTS", "Email already exists"),
    USERNAME_ALREADY_EXISTS(409, "USERNAME_ALREADY_EXISTS", "Username already exists"),

    // 500 Internal Server Errors
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR", "An unexpected error occurred"),
    DATABASE_ERROR(500, "DATABASE_ERROR", "Database operation failed"),
    EXTERNAL_SERVICE_ERROR(500, "EXTERNAL_SERVICE_ERROR", "External service error"),
    EMAIL_SENDING_ERROR(500, "EMAIL_SENDING_ERROR", "Failed to send email");

    /**
     * HTTP status code associated with this error.
     */
    private final int httpStatusCode;

    /**
     * Error code identifier.
     */
    private final String code;

    /**
     * Default error message.
     */
    private final String message;

    /**
     * Constructor for ErrorCode enum.
     *
     * @param httpStatusCode the HTTP status code
     * @param code           the error code identifier
     * @param message        the default error message
     */
    ErrorCode(int httpStatusCode, String code, String message) {
        this.httpStatusCode = httpStatusCode;
        this.code = code;
        this.message = message;
    }


    /**
     * Check if this error code represents a client error (4xx).
     *
     * @return true if client error, false otherwise
     */
    public boolean isClientError() {
        return httpStatusCode >= 400 && httpStatusCode < 500;
    }

    /**
     * Check if this error code represents a server error (5xx).
     *
     * @return true if server error, false otherwise
     */
    public boolean isServerError() {
        return httpStatusCode >= 500 && httpStatusCode < 600;
    }

    /**
     * Check if this error code represents a validation error.
     *
     * @return true if validation error, false otherwise
     */
    public boolean isValidationError() {
        return this == VALIDATION_ERROR || this == INVALID_REQUEST || this == BAD_REQUEST;
    }

    /**
     * Check if this error code represents an authentication error.
     *
     * @return true if authentication error, false otherwise
     */
    public boolean isAuthenticationError() {
        return this == UNAUTHORIZED || this == AUTHENTICATION_FAILED || this == INVALID_CREDENTIALS
                || this == TOKEN_EXPIRED || this == INVALID_TOKEN;
    }

    /**
     * Check if this error code represents an authorization error.
     *
     * @return true if authorization error, false otherwise
     */
    public boolean isAuthorizationError() {
        return this == FORBIDDEN || this == INSUFFICIENT_PERMISSIONS || this == ACCESS_DENIED;
    }

    @Override
    public String toString() {
        return code;
    }
}

