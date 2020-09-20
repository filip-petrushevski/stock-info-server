package com.stockinfo.services;

import com.stockinfo.models.Company;
import com.stockinfo.models.OHLC;
import com.stockinfo.models.dto.UserCompanySubcriptionDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface CompanyService {
    ResponseEntity<Map<String, Object>> getAllCompanies(String query, int page, int size);

    List<OHLC> getAllOhlcForCompany(String companySymbol);

    Company getCompany(String companySymbol);

    List<String> getCompanySymbolsForUser(Long userId);

    ResponseEntity subscribeUserToCompany(UserCompanySubcriptionDto userCompanyDto);

    ResponseEntity deleteUserToCompanySupscription(String companySymbol, Long userId);

    List<Company> getCompaniesForUser(Long userId);

    List<Company> getFilteredCompaniesForUser(Long userId, String searchString);
}
