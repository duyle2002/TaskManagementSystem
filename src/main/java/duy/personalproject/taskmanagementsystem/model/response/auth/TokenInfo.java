package duy.personalproject.taskmanagementsystem.model.response.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenInfo {
    private String token;
    private long expiresAt;
}
