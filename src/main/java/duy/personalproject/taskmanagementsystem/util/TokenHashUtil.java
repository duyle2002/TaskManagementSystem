package duy.personalproject.taskmanagementsystem.util;

import duy.personalproject.taskmanagementsystem.exception.BusinessException;
import duy.personalproject.taskmanagementsystem.model.constant.HashAlgorithmConstants;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class TokenHashUtil {
    private TokenHashUtil() {}

    public static String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance(HashAlgorithmConstants.SHA256);
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new BusinessException("SHA-256 algorithm not found", e);
        }
    }
}