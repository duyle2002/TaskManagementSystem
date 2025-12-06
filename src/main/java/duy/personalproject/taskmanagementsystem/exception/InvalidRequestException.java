package duy.personalproject.taskmanagementsystem.exception;

/**
 * Exception thrown when an invalid request is made.
 * Used for HTTP 400 Bad Request responses.
 */
public class InvalidRequestException extends BusinessException {

    public InvalidRequestException(String message) {
        super(message, ErrorCode.INVALID_REQUEST);
    }

    public InvalidRequestException(String fieldName, String reason) {
        super(String.format("Invalid request: field '%s' - %s", fieldName, reason),
                ErrorCode.INVALID_REQUEST);
    }
}

