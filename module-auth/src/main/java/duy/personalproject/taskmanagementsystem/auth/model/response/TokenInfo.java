package duy.personalproject.taskmanagementsystem.auth.model.response;

public record TokenInfo(
        String token,
        long expiresAt
){}