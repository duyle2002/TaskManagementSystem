package duy.personalproject.taskmanagementsystem.unit.util;

import duy.personalproject.taskmanagementsystem.auth.util.TokenHashUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for TokenHashUtil.
 * Tests the SHA-256 hashing functionality without any Spring context.
 */
@DisplayName("TokenHashUtil Unit Tests")
class TokenHashUtilTest {

    @Test
    @DisplayName("Should hash token successfully using SHA-256")
    void hashToken_ValidToken_ReturnsHashedString() {
        String token = "test-token-12345";

        String hashedToken = TokenHashUtil.hashToken(token);

        assertThat(hashedToken)
                .isNotNull()
                .isNotEmpty()
                .hasSize(64) // SHA-256 produces 64 character hex string
                .matches("^[a-f0-9]{64}$"); // Only lowercase hex characters
    }

    @Test
    @DisplayName("Should produce same hash for same token")
    void hashToken_SameToken_ProducesSameHash() {
        String token = "test-token-12345";

        String hash1 = TokenHashUtil.hashToken(token);
        String hash2 = TokenHashUtil.hashToken(token);

        assertThat(hash1).isEqualTo(hash2);
    }

    @Test
    @DisplayName("Should produce different hash for different tokens")
    void hashToken_DifferentTokens_ProducesDifferentHashes() {
        String token1 = "test-token-1";
        String token2 = "test-token-2";

        String hash1 = TokenHashUtil.hashToken(token1);
        String hash2 = TokenHashUtil.hashToken(token2);

        assertThat(hash1).isNotEqualTo(hash2);
    }

    @Test
    @DisplayName("Should handle empty string")
    void hashToken_EmptyString_ReturnsHash() {
        String token = "";

        String hashedToken = TokenHashUtil.hashToken(token);

        assertThat(hashedToken)
                .isNotNull()
                .hasSize(64);
    }

    @Test
    @DisplayName("Should handle special characters")
    void hashToken_SpecialCharacters_ReturnsHash() {
        String token = "!@#$%^&*()_+-=[]{}|;:',.<>?/~`";

        String hashedToken = TokenHashUtil.hashToken(token);

        assertThat(hashedToken)
                .isNotNull()
                .hasSize(64)
                .matches("^[a-f0-9]{64}$");
    }

    @Test
    @DisplayName("Should handle very long strings")
    void hashToken_VeryLongString_ReturnsHash() {
        String token = "a".repeat(10000);

        String hashedToken = TokenHashUtil.hashToken(token);

        assertThat(hashedToken)
                .isNotNull()
                .hasSize(64)
                .matches("^[a-f0-9]{64}$");
    }
}

