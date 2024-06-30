package com.example.productservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Getter
@Setter
@MappedSuperclass
//@EntityListeners(AuditingEntityListener.class)
public class Auditor extends InitializationInfo {

    @LastModifiedBy
    @Column(name = "updated_by", updatable = false)
    private String updatedBy;

    @LastModifiedDate
    @Column(name = "last_modified_date", updatable = false)
    private Date lastModifiedDate;
}
