package com.stockinfo.repository;

import com.stockinfo.models.ERole;
import com.stockinfo.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
