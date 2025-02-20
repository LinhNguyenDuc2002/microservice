package com.example.userservice.security.data;

import com.example.userservice.entity.Role;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class AuthUser implements UserDetails {
    @Setter
    @Getter
    @Id
    private String id;

    @NonNull
    private String username;

    @NonNull
    private String password;

    @Setter
    @Getter
    private String email;

    @Setter
    @Getter
    private String refreshToken;

    @Setter
    @Getter
    private boolean status;

    private Collection<? extends GrantedAuthority> authorities;

    public AuthUser(String id, String username, String password, Collection<Role> authorityList, boolean status) {
        super();
        this.id = id;
        this.username = username;
        this.password = password;
        this.status = status;
        this.authorities = authorityList
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.status;
    }
}
