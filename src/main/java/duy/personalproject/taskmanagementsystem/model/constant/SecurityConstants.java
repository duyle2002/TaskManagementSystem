package duy.personalproject.taskmanagementsystem.model.constant;

public class SecurityConstants {
    private SecurityConstants() {
    }

    public static final String HAS_ROLE_ADMIN = "hasRole('ROLE_ADMIN')";
    public static final String HAS_ROLE_USER = "hasRole('ROLE_USER')";

    public static final String HAS_ROLE_USER_OR_ADMIN = "hasAnyRole('ROLE_USER', 'ROLE_ADMIN')";
}
