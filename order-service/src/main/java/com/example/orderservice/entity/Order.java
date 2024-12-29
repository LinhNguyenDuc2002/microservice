package com.example.orderservice.entity;

import com.example.orderservice.constant.OrderStatus;
import com.example.orderservice.converter.OrderStatusConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.Collection;

@Entity
@Data
@Table(name = "order")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order extends Auditor {
    @Id
    @UuidGenerator
    @Column(name = "id")
    private String id;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "status")
    @Convert(converter = OrderStatusConverter.class)
    private OrderStatus status;

    @OneToMany(mappedBy = "order")
    @EqualsAndHashCode.Exclude
    private Collection<OrderDetail> orderDetails;

    @OneToOne
    @JoinColumn(name = "receiver_id")
    private Receiver receiver;
}
