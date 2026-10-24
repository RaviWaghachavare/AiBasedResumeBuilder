
package com.switchproject.demo.service;

import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    private final SecureRandom secureRandom = new SecureRandom();

    private final Map<String, OtpData> otpStorage =
            new ConcurrentHashMap<>();

    private static final long OTP_EXPIRY_SECONDS = 300;
    private static final int MAX_ATTEMPTS = 5;

    public String generateOtp(String email) {
        String key = normalizeEmail(email);

        String otp = String.format(
                "%06d",
                secureRandom.nextInt(1_000_000)
        );

        otpStorage.put(
                key,
                new OtpData(
                        otp,
                        Instant.now().plusSeconds(OTP_EXPIRY_SECONDS)
                )
        );

        return otp;
    }

    public boolean verifyOtp(String email, String otp) {
        String key = normalizeEmail(email);
        OtpData data = otpStorage.get(key);

        if (data == null) {
            return false;
        }

        synchronized (data) {
            if (otpStorage.get(key) != data) {
                return false;
            }

            if (Instant.now().isAfter(data.expiry)) {
                otpStorage.remove(key, data);
                return false;
            }

            if (data.attempts >= MAX_ATTEMPTS) {
                otpStorage.remove(key, data);
                return false;
            }

            data.attempts++;

            if (!MessageDigest.isEqual(
                    data.otp.getBytes(java.nio.charset.StandardCharsets.UTF_8),
                    otp.getBytes(java.nio.charset.StandardCharsets.UTF_8))) {

                if (data.attempts >= MAX_ATTEMPTS) {
                    otpStorage.remove(key, data);
                }

                return false;
            }

            otpStorage.remove(key, data);
            return true;
        }
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(java.util.Locale.ROOT);
    }

    private static class OtpData {
        private final String otp;
        private final Instant expiry;
        private int attempts = 0;

        private OtpData(String otp, Instant expiry) {
            this.otp = otp;
            this.expiry = expiry;
        }
    }
}
