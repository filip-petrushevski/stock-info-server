package com.stockinfo.repository;

import com.stockinfo.models.StockUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<StockUser, Long> {
    Optional<StockUser> findByUsername(String username);
    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

}
