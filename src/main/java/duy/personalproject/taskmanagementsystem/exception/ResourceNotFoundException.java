package duy.personalproject.taskmanagementsystem.exception;

/**
 * Exception thrown when a requested resource is not found.
 */
public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(String message) {
        super(message, ErrorCode.RESOURCE_NOT_FOUND);
    }

    public ResourceNotFoundException(String resourceName, Long id) {
        this(resourceName, "id", id);
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s: %s", resourceName, fieldName, fieldValue),
                ErrorCode.RESOURCE_NOT_FOUND);
    }
}