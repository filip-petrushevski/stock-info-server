package com.stockinfo.repository;

import com.stockinfo.models.Company;
import com.stockinfo.models.StockUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, String> {
    Page<Company> findBySymbolContainingIgnoreCaseOrNameContainingIgnoreCaseOrderBySymbol(
            String str, String str2, Pageable pageable);
    Page<Company> findAllByOrderBySymbol(Pageable pageable);
    List<Company> findAllByUsers_Id(Long userId);
}