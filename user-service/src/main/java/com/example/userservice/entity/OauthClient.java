package com.example.userservice.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

/**
 * Save registered client info
 * Before a client app can use services of the OAuth 2.0 provider, it needs to be registered
 * The information of clients includes ID client, client secret, scope, redirect URI, ... will be saved in RegisteredClientRepository.
 */
@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "oauth_client")
public class OauthClient extends Auditor {
    @Id
    @UuidGenerator
    @Column(name = "uuid")
    private String uuid;

    @Column(name = "client_id", nullable = false, unique = true)
    private String clientId;

    @Column(name = "client_secret")
    private String clientSecret;

    @Column(name = "client_type")
    private String clientType;

    @Column(name = "description")
    private String description;

    @OneToOne(mappedBy = "oauthId", cascade = CascadeType.REMOVE)
    @PrimaryKeyJoinColumn
    private OauthClientDetail detail;
}
