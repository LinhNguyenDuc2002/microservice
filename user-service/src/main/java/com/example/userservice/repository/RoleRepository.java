package com.example.userservice.repository;

import com.example.userservice.constant.RoleType;
import com.example.userservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Role findByRoleName(RoleType roleName);
}
