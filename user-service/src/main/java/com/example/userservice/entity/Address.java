package com.example.userservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Data
@Builder
@Table(name = "address")
@NoArgsConstructor
@AllArgsConstructor
public class Address extends Auditor {
    @Id
    @UuidGenerator
    private String id;

    @Column(name = "detail")
    private String detail;

    private String ward;

    private String district;

    private String city;

    private String country;
}
