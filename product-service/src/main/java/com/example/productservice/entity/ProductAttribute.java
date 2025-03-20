package com.example.productservice.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "product_attribute")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttribute {
    @EmbeddedId
    private ProductAttributeId id;

    @ManyToOne
    @JoinColumn(name = "product_type_id")
    @MapsId("productTypeId")
    private ProductType productType;

    @ManyToOne
    @JoinColumn(name = "attribute_id")
    @MapsId("attributeId")
    private Attribute attribute;

    private Integer level;

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductAttributeId implements Serializable {
        private String productTypeId;

        private String attributeId;
    }
}
