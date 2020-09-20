package com.stockinfo.controllers;

import com.stockinfo.models.Company;
import com.stockinfo.models.OHLC;
import com.stockinfo.models.dto.UserCompanySubcriptionDto;
import com.stockinfo.services.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController()
@RequestMapping(path = "/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping()
    public ResponseEntity<Map<String, Object>> getAllCompanies(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return companyService.getAllCompanies(query, page, size);
    }

    @GetMapping(path = "{symbol}/ohlc")
    public List<OHLC> getAllOhlcForCompany(@PathVariable(name = "symbol") String companySymbol) {
        return this.companyService.getAllOhlcForCompany(companySymbol);
    }

    @GetMapping(path = "{symbol}")
    public Company getCompany(@PathVariable(name = "symbol") String companySymbol) {
        return this.companyService.getCompany(companySymbol);
    }

    @GetMapping(path = "/symbols/forUser/{id}")
    public List<String> getCompanySymbolsForUser(@PathVariable(name = "id") Long userId) {
        return this.companyService.getCompanySymbolsForUser(userId);
    }

    @GetMapping(path = "/forUser/{id}")
    public List<Company> getCompaniesForUser(@PathVariable(name = "id") Long userId, @RequestParam String searchString) {
        return this.companyService.getFilteredCompaniesForUser(userId, searchString);
    }

    @PostMapping(path = "subscribe")
    public ResponseEntity subscribeUserToCompany(@RequestBody UserCompanySubcriptionDto userCompanyDto) {
        return this.companyService.subscribeUserToCompany(userCompanyDto);
    }

    @DeleteMapping(path = "subscribe/{companySymbol}/{userId}")
    public ResponseEntity deleteUserToCompanySupscription(@PathVariable String companySymbol,@PathVariable Long userId) {
        return this.companyService.deleteUserToCompanySupscription(companySymbol, userId);
    }
}
