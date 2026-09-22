package com.jvmcrew.util;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class PasswordUtil {

    private static final int ITERATIONS = 65_536;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = 16;

    private PasswordUtil() {
    }

    public static String hashPassword(String password) {
        try {
            byte[] salt = new byte[SALT_LENGTH];
            new SecureRandom().nextBytes(salt);

            PBEKeySpec spec = new PBEKeySpec(
                    password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);

            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            byte[] hash = factory.generateSecret(spec).getEncoded();
            spec.clearPassword();

            return ITERATIONS + ":"
                    + Base64.getEncoder().encodeToString(salt) + ":"
                    + Base64.getEncoder().encodeToString(hash);

        } catch (Exception e) {
            throw new IllegalStateException("Password hashing failed.", e);
        }
    }

    public static boolean verifyPassword(String password, String storedValue) {
        try {
            String[] parts = storedValue.split(":", -1);
            if (parts.length != 3) {
                return false;
            }

            int iterations = Integer.parseInt(parts[0]);
            byte[] salt = Base64.getDecoder().decode(parts[1]);
            byte[] expected = Base64.getDecoder().decode(parts[2]);

            PBEKeySpec spec = new PBEKeySpec(
                    password.toCharArray(), salt, iterations, expected.length * 8);

            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            byte[] actual = factory.generateSecret(spec).getEncoded();
            spec.clearPassword();

            return MessageDigest.isEqual(actual, expected);

        } catch (Exception e) {
            return false;
        }
    }
}
