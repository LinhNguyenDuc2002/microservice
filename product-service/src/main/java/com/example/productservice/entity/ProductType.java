package com.example.productservice.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.Collection;

@Entity
@Getter
@Setter
@Table(name = "product_type")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductType extends Auditor {
    @Id
    @UuidGenerator
    @Column(name = "id")
    private String id;

    @Column(name = "image_id")
    private String imageId;

    @Column(name = "price")
    private Double price;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @EqualsAndHashCode.Exclude
    private Product product;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_type_attribute",
            joinColumns = @JoinColumn(name = "product_type_id"),
            inverseJoinColumns = @JoinColumn(name = "attribute_id")
    )
    private Collection<Attribute> attributes;
}
