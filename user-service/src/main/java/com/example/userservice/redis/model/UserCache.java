package com.example.userservice.redis.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.Date;

@RedisHash(timeToLive = 1800L)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCache implements Serializable {
    @Id
    @Indexed
    private String id;

    private String secretKey;

    private String nickname;

    private String username;

    private String password;

    private String fullname;

    private Date dob;

    private String email;

    private String phone;

    private String otp;

    private String role;
}
