package com.example.servicefoundation.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OTPUtil {
    private static final Integer TIME_STEP = 60;

    private static final Integer DIGITS = 6;

    public static String generateOTP() throws NoSuchAlgorithmException, InvalidKeyException {
        // Generate secret key
        byte[] secretKey = generateKey();

        // Get present time and divide by TIME_STEP
        long timestamp = Instant.now().getEpochSecond() / TIME_STEP;

        byte[] msg = ByteBuffer.allocate(8).putLong(timestamp).array();
        byte[] hash = hmacSha1(secretKey, msg);
        int offset = hash[hash.length - 1] & 0xF;
        int binary = ((hash[offset] & 0x7F) << 24) | ((hash[offset + 1] & 0xFF) << 16) |
                ((hash[offset + 2] & 0xFF) << 8) | (hash[offset + 3] & 0xFF);
        int otp = binary % (int) Math.pow(10, DIGITS);
        return String.format("%0" + DIGITS + "d", otp);
    }

    private static byte[] generateKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);

        // make the secret key more human-readable by lower-casing and inserting spaces between each group of 4 characters
        return bytes;
    }

    private static byte[] hmacSha1(byte[] key, byte[] message) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmac = Mac.getInstance("HmacSHA1");
        SecretKeySpec keySpec = new SecretKeySpec(key, "HmacSHA1");
        hmac.init(keySpec);
        return hmac.doFinal(message);
    }
}
