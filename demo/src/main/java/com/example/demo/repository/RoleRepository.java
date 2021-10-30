package com.example.demo.repository;

import com.example.demo.entity.RoleName;
import com.example.demo.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Roles, Integer> {
    Optional<Roles> findByName(RoleName roleName);
}
