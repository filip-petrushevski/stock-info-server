package com.stockinfo.models.dto;

import lombok.Data;

@Data
public class UserCompanySubcriptionDto {
    private Long userId;
    private String companySymbol;

    public UserCompanySubcriptionDto(Long userId, String companySymbol) {
        this.userId = userId;
        this.companySymbol = companySymbol;
    }
}
