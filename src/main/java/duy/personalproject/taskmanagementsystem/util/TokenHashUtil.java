package duy.personalproject.taskmanagementsystem.util;

import duy.personalproject.taskmanagementsystem.exception.BusinessException;
import duy.personalproject.taskmanagementsystem.model.constant.HashAlgorithmConstant;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TokenHashUtil {
    private TokenHashUtil() {}

    public static String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance(HashAlgorithmConstant.SHA256);
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new BusinessException("SHA-256 algorithm not found", e);
        }
    }
}