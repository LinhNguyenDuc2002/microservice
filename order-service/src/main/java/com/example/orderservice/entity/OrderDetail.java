package com.example.orderservice.entity;

import com.example.orderservice.constant.OrderDetailStatus;
import com.example.orderservice.converter.OrderDetailStatusConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Table(name = "order_detail")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail extends Auditor {
    @Id
    @UuidGenerator
    @Column(name = "id")
    private String id;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "unit_price")
    private Double unitPrice;

    @Column(name = "product_id")
    private String productDetailId;

    @Column(name = "comment_status")
    private Boolean commentStatus;

    @Column(name = "status")
    @Convert(converter = OrderDetailStatusConverter.class)
    private OrderDetailStatus status;

    @JoinColumn(name = "customer_id")
    private String customerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @EqualsAndHashCode.Exclude
    private PurchaseOrder purchaseOrder;
}
