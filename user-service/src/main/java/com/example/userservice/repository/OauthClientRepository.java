package com.example.userservice.repository;

import com.example.userservice.entity.OauthClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OauthClientRepository extends JpaRepository<OauthClient, String> {
    OauthClient findByClientId(String clientId);
}
