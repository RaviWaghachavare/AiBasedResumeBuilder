
package com.switchproject.demo.service;

import org.springframework.stereotype.Service;

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

    public String generateOtp(String email) {
        String otp = String.format(
                "%06d",
                secureRandom.nextInt(1_000_000)
        );

        otpStorage.put(
                email.toLowerCase(),
                new OtpData(
                        otp,
                        Instant.now().plusSeconds(OTP_EXPIRY_SECONDS)
                )
        );

        return otp;
    }

    public boolean verifyOtp(String email, String otp) {
        String key = email.toLowerCase();
        OtpData data = otpStorage.get(key);

        if (data == null) {
            return false;
        }

        if (Instant.now().isAfter(data.expiry)) {
            otpStorage.remove(key);
            return false;
        }

        if (!data.otp.equals(otp)) {
            return false;
        }

        otpStorage.remove(key);
        return true;
    }

    private static class OtpData {
        private final String otp;
        private final Instant expiry;

        private OtpData(String otp, Instant expiry) {
            this.otp = otp;
            this.expiry = expiry;
        }
    }
}
