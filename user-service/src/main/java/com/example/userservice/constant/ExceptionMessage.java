package com.example.userservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionMessage {
    public final static String ERROR_INPUT_INVALID = "The request is invalid";

    public final static String ERROR_USER_NOT_FOUND = "User doesn't exist";
    public final static String ERROR_USERNAME_EXISTED = "Username existed";
    public final static String ERROR_USER_UNKNOWN = "";
    public final static String ERROR_USER_ASSIGN_ROLE_ALREADY = "User have already been assigned this role";

    public final static String ERROR_EMAIL_EXISTED = "Email already exists";
    public final static String ERROR_EMAIL_INVALID = "Email is invalid";

    public final static String ERROR_INVALID_OTP = "This is invalid otp";

    public final static String ERROR_INPUT_PASSWORD_INVALID = "Input password is invalid";
    public final static String ERROR_OLD_PASSWORD_INVALID = "Old password is invalid";

    public final static String ERROR_ROLE_INVALID = "Role is invalid";
}
