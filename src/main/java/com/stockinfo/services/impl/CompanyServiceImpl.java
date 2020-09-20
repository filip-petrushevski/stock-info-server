package com.stockinfo.services.impl;

import com.stockinfo.models.Company;
import com.stockinfo.models.OHLC;
import com.stockinfo.models.StockUser;
import com.stockinfo.models.dto.UserCompanySubcriptionDto;
import com.stockinfo.repository.CompanyRepository;
import com.stockinfo.repository.OHLCRepository;
import com.stockinfo.repository.UserRepository;
import com.stockinfo.services.CompanyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final OHLCRepository ohlcRepository;
    private final UserRepository userRepository;

    private static final String ACTION_ADD = "add";
    private static final String ACTION_DELETE = "delete";

    public CompanyServiceImpl(CompanyRepository companyRepository,
                          OHLCRepository ohlcRepository,
                          UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.ohlcRepository = ohlcRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<Map<String, Object>> getAllCompanies(String query, int page, int size) {
        try {
            List<Company> companies;
            Pageable paging = PageRequest.of(page, size);

            Page<Company> pageCompanies;
            if (query == null) {
                pageCompanies = companyRepository.findAllByOrderBySymbol(paging);
            }
            else {
                pageCompanies =
                        companyRepository.findBySymbolContainingIgnoreCaseOrNameContainingIgnoreCaseOrderBySymbol(
                                query, query, paging);
            }

            companies = pageCompanies.getContent();

            if (companies.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("companies", companies);
            response.put("currentPage", pageCompanies.getNumber());
            response.put("totalItems", pageCompanies.getTotalElements());
            response.put("totalPages", pageCompanies.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<OHLC> getAllOhlcForCompany(String companySymbol) {
        return this.ohlcRepository.findAllByCompanySymbol(companySymbol);
    }

    @Override
    public Company getCompany(String companySymbol) {
        return this.companyRepository.findById(companySymbol).get();
    }

    @Override
    public List<String> getCompanySymbolsForUser(Long userId) {
        return this.companyRepository.findAllByUsers_Id(userId).stream()
                .map(Company::getSymbol)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ResponseEntity subscribeUserToCompany(UserCompanySubcriptionDto userCompanyDto) {
        return userToCompanySubscription(userCompanyDto.getCompanySymbol(), userCompanyDto.getUserId(), ACTION_ADD);
    }

    @Transactional
    @Override
    public ResponseEntity deleteUserToCompanySupscription(String companySymbol, Long userId) {
        return userToCompanySubscription(companySymbol, userId, ACTION_DELETE);
    }

    @Override
    public List<Company> getCompaniesForUser(Long userId) {
        return this.companyRepository.findAllByUsers_Id(userId);
    }

    @Override
    public List<Company> getFilteredCompaniesForUser(Long userId, String searchString) {
        if (searchString == null || searchString.isEmpty()) {
            return getCompaniesForUser(userId);
        }
        String searchStringLc = searchString.toLowerCase();
        return this.companyRepository.findAllByUsers_Id(userId).stream()
                .filter(company -> company.getSymbol().toLowerCase().contains(searchStringLc) ||
                        company.getName().toLowerCase().contains(searchStringLc))
                .collect(Collectors.toList());
    }

    private ResponseEntity userToCompanySubscription(String companySymbol, Long userId, String action) {
        Optional<Company> company = this.companyRepository.findById(companySymbol);
        if(!company.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<StockUser> stockUser = this.userRepository.findById(userId);
        if(!stockUser.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(action.equals(ACTION_DELETE)) {
            stockUser.get().getCompanies().remove(company.get());
        } else if (action.equals(ACTION_ADD)) {
            stockUser.get().getCompanies().add(company.get());
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        this.userRepository.saveAndFlush(stockUser.get());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
