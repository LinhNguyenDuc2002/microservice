package com.example.userservice.dto;

import com.example.userservice.constant.RoleType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class RoleDto {
    private String id;

    private String description;

    @JsonProperty("role_name")
    private RoleType roleName;
}
