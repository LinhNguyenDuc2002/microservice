package com.example.orderservice.entity;

import com.example.orderservice.constant.BillStatus;
import com.example.orderservice.constant.GeneralConstant;
import com.example.orderservice.converter.BillStatusConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.Collection;
import java.util.Date;

@Entity
@Data
@Table(name = "bill")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bill extends Auditor {
    @Id
    @UuidGenerator
    @Column(name = "id")
    private String id;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "phone")
    private String phone;

    @Column(name = "status")
    @Convert(converter = BillStatusConverter.class)
    private BillStatus status;

    @Column(name = "shop_id")
    private String shopId;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "bill")
    @EqualsAndHashCode.Exclude
    private Collection<Detail> details;

    @PrePersist
    private void setCode() {
        Date date = super.getCreatedDate();
        this.code = String.format("%s%02d%02d%04d_%04d", "B", date.getDay(), date.getMonth(), date.getYear(), GeneralConstant.billCode);
        GeneralConstant.billCode += 1;
    }
}
