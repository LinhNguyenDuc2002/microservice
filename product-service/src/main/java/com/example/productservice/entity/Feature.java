package com.example.productservice.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name = "feature")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Feature extends Auditor {
    @UuidGenerator
    @Id
    private String id;

    private String name;

    @OneToMany(mappedBy = "feature", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    private Collection<Attribute> attributes;

    @OneToMany(mappedBy = "feature", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    private Collection<ProductFeature> productFeatures;
}