package com.stockinfo.models;

import lombok.Data;

@Data
public class CompanySymbol {
    private String description;
    private String displaySymbol;
    private String symbol;
    private String type;
    private String currency;
}
