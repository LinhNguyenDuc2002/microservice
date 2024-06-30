package com.example.userservice.entity;

import jakarta.persistence.Column;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

public class Auditor extends InitializationInfo {
    @LastModifiedBy
    @Column(name = "updated_by", updatable = false)
    private String updatedBy;

    @LastModifiedDate
    @Column(name = "last_modified_date", updatable = false)
    private Date lastModifiedDate;
}
