package duy.personalproject.taskmanagementsystem.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "task.management.system.refresh-token")
public class RefreshTokenConfigProperties {
    private int staleTimeInDays;
    private int cleanupBatchSize;
}
