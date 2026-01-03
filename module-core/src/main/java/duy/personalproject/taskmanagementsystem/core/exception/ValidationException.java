package duy.personalproject.taskmanagementsystem.core.exception;

/**
 * Exception thrown when input validation fails.
 */
public class ValidationException extends BusinessException {

    public ValidationException(String message) {
        super(message, ErrorCode.VALIDATION_ERROR);
    }

    public ValidationException(String fieldName, String message) {
        super(String.format("Validation failed for field '%s': %s", fieldName, message),
                ErrorCode.VALIDATION_ERROR);
    }
}

