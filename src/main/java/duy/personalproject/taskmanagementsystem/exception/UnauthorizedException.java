package duy.personalproject.taskmanagementsystem.exception;

/**
 * Exception thrown when user is not authenticated.
 */
public class UnauthorizedException extends BusinessException {

    public UnauthorizedException(String message) {
        super(message, ErrorCode.UNAUTHORIZED);
    }

    public UnauthorizedException() {
        super("User is not authenticated", ErrorCode.UNAUTHORIZED);
    }
}

