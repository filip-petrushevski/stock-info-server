package com.stockinfo.repository;

import com.stockinfo.models.OHLC;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OHLCRepository extends JpaRepository<OHLC, Long> {
    List<OHLC> findAllByCompanySymbol(String symbol);
}
