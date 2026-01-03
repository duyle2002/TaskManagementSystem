package duy.personalproject.taskmanagementsystem.auth.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtConfigProperties {
    private String secretKey;
    private Long accessTokenExpirationInSecond;
    private Long refreshTokenExpirationInSecond;
}
