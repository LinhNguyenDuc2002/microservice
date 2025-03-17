package com.example.productservice.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Auditor extends InitializationInfo {
    private String updatedBy;

    private Date lastModifiedDate;
}
