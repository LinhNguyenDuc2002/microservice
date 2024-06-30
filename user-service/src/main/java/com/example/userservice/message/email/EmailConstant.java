package com.example.userservice.message.email;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Define key arguments using email
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EmailConstant {
    public final static String TEMPLATE_EMAIL_VERIFY_OTP = "verify-otp.html";

    public final static String ARG_LOGO_URI = "ARG_LOGO_URI";
    public final static String ARG_OTP_CODE = "ARG_OTP_CODE";
    public final static String ARG_RECEIVER_NAME = "ARG_RECEIVER_NAME";
    public final static String ARG_SUPPORT_EMAIL = "ARG_SUPPORT_EMAIL";
    public final static String ARG_VERIFY_EMAIL_SUBJECT = "[Blue shop] Verify account";
}
