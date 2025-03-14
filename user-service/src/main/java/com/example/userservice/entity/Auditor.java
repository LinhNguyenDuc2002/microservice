package com.example.userservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Auditor extends InitializationInfo {
    @LastModifiedBy
    @Column(name = "updated_by", updatable = false)
    private String updatedBy;

    @LastModifiedDate
    @Column(name = "last_modified_date", updatable = false)
    private Date lastModifiedDate;
}
