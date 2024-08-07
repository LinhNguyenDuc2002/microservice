package com.example.orderservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Table(name = "detail")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Detail extends Auditor {
    @Id
    @UuidGenerator
    @Column(name = "id")
    private String id;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "unit_price")
    private Double unitPrice;

    @Column(name = "product_id")
    private String product;

    @Column(name = "product_type_id")
    private String productType;

    @Column(name = "comment_status")
    private Boolean commentStatus;

    @Column(name = "status")
    private Boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @EqualsAndHashCode.Exclude
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id")
    @EqualsAndHashCode.Exclude
    private Bill bill;
}
