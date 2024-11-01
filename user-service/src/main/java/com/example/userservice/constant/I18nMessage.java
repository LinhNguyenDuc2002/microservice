package com.example.userservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class I18nMessage {
    /**
     * Level: INFO
     */
    // User
    public final static String INFO_CREATE_ACCOUNT = "info.create-account";
    public final static String INFO_DELETE_ACCOUNT = "info.delete-account";
    public final static String INFO_GET_ALL_USER = "info.get-all-user";
    public final static String INFO_GET_USER = "info.get-user";
    public final static String INFO_UPDATE_USER = "info.update-user";
    public final static String INFO_CHANGE_PASSWORD = "info.change-password";
    public final static String INFO_RESET_PASSWORD = "info.reset-password";
    public final static String INFO_WAIT_OTP = "info.wait-otp";
    public final static String INFO_GET_ADDRESS = "info.get-address";
    public final static String INFO_UPDATE_ADDRESS = "info.update-address";
    public final static String INFO_SET_AVATAR = "info.set-avatar";
    public final static String INFO_DELETE_AVATAR = "info.delete-avatar";
    public final static String INFO_GET_ALL_ROLE = "info.get-all-role";
    public final static String INFO_GET_ROLE = "info.get-role";
    public final static String INFO_UPDATE_ROLE = "info.update-role";
    public final static String INFO_GET_ALL_PERMISSION = "info.get-all-permission";
    public final static String INFO_GET_PERMISSION = "info.get-permission";
    public final static String INFO_UPDATE_PERMISSION = "info.update-permission";

    /**
     * Level: WARN
     */
    public final static String a = "a";

    /**
     * Level: ERROR
     */
    public final static String ERROR_DATA_INVALID = "error.data.invalid";
    public final static String ERROR_USER_CACHE_NOT_FOUND = "error.user-cache.not-found";
    public final static String ERROR_USER_NOT_FOUND = "error.user.not-found";
    public final static String ERROR_USERNAME_EXISTED = "error.username.existed";
    public final static String ERROR_USER_UNKNOWN = "";
    public final static String ERROR_USER_ASSIGN_ROLE = "error.user-assigned-role";
    public final static String ERROR_EMAIL_EXISTED = "error.email.existed";
    public final static String ERROR_EMAIL_INVALID = "error.email.invalid";
    public final static String ERROR_INVALID_OTP = "error.otp.invalid";
    public final static String ERROR_PASSWORD_INVALID = "error.password.invalid";
    public final static String ERROR_OLD_PASSWORD_WRONG = "error.old-password.wrong";
    public final static String ERROR_ROLE_INVALID = "error.role.invalid";
    public final static String ERROR_ROLE_NOT_FOUND = "error.role.not-found";
    public final static String ERROR_PERMISSION_NOT_FOUND = "error.permission.not-found";
    public final static String ERROR_PERMISSION_CODE_EXISTED = "error.permission-code.existed";
    public final static String ERROR_IMAGE_NOT_FOUND = "error.image.not-found";
}
