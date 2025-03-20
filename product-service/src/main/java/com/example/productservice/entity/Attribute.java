package com.example.productservice.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.Collection;

@Data
@Entity
@Table(name = "attribute")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attribute extends Auditor {
    @UuidGenerator
    @Id
    private String id;

    private String value;

    @ManyToOne
    @JoinColumn(name = "feature_id")
    @EqualsAndHashCode.Exclude
    private Feature feature;

    @OneToMany(mappedBy = "attribute", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    private Collection<ProductAttribute> productAttributes;
}