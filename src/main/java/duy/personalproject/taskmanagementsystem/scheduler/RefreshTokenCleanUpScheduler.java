package duy.personalproject.taskmanagementsystem.scheduler;

import duy.personalproject.taskmanagementsystem.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "REFRESH_TOKEN_CLEANUP_SCHEDULER")
public class RefreshTokenCleanUpScheduler {
    private final RefreshTokenService refreshTokenService;

    /**
     * Scheduled task to clean up revoked and expired refresh tokens before they reach stale time in the database.
     * This method is executed based on the cron expression defined in the application properties.
     */
    @Scheduled(cron = "${task.management.system.cron.clean-up-expired-and-revoked-tokens.expression}")
    public void cleanUpExpiredAndRevokedTokens() {
        log.info("Cleaning up expired and revoked refresh tokens");
        refreshTokenService.cleanUpExpiredAndRevokedTokens();
    }
}
