package com.example.userservice.util;

import java.util.Random;

public final class OtpUtil {
    public static String generateOTP() {
        Random random = new Random();

        int min = 100000;
        int max = 999999;
        Integer otp = random.nextInt(max-min)+100000;

        return otp.toString();
    }
}
