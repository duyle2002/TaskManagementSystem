package duy.personalproject.taskmanagementsystem.core.exception;

/**
 * Exception thrown when user doesn't have permission to perform an action.
 */
public class ForbiddenException extends BusinessException {

    public ForbiddenException(String message) {
        super(message, ErrorCode.FORBIDDEN);
    }

    public ForbiddenException() {
        super("You don't have permission to perform this action", ErrorCode.FORBIDDEN);
    }

    public ForbiddenException(String resource, String action) {
        super(String.format("You don't have permission to %s %s", action, resource),
                ErrorCode.FORBIDDEN);
    }
}

