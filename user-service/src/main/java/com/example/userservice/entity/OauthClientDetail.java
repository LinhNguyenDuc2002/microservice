package com.example.userservice.entity;

import com.example.userservice.converter.StringListConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "oauth_client_detail")
public class OauthClientDetail {
    @Id
    @Column(name = "oauth_id")
    private String id;

    @Column(name = "web_origins")
    @Convert(converter = StringListConverter.class)
    private List<String> webOrigins;

    @Column(name = "redirect_uris")
    @Convert(converter = StringListConverter.class)
    private List<String> redirectUris;

    @Column(name = "access_token_lifespan")
    private Long tokenLifespanInMinute;

    @Column(name = "refresh_token_lifespan")
    private Long refreshTokenLifespanInMinute;

    @OneToOne
    @MapsId
    @JoinColumn(name = "oauth_id", foreignKey = @ForeignKey(name = "fk_oauth_client"))
    private OauthClient oauthId;
}
