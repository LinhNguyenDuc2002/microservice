package com.example.userservice.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseMessage {
    CREATE_USER_SUCCESS("Created a user successfully"),

    DELETE_USER_SUCCESS("Deleted a user successfully"),

    GET_ALL_USER_SUCCESS("Got all users successfully"),

    GET_USER_SUCCESS("Got a user successfully"),

    CHANGE_PASSWORD_SUCCESS("Changed password successfully"),

    RESET_PASSWORD_SUCCESS("Reset password successfully"),

    WAIT_ENTER_OTP("Waiting for enter otp"),

    UPDATE_USER_SUCCESS("Update a user successfully"),

    GET_ADDRESS_SUCCESS("Get address successfully"),

    UPDATE_ADDRESS_SUCCESS("Update address successfully"),

    SET_AVATAR_SUCCESS(""),

    DELETE_AVATAR_SUCCESS("");

    private String message;
}
