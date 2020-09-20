package com.stockinfo.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Company {
    @Id
    @Size(max = 256)
    private String symbol;
    @Size(max = 256)
    private String country;
    @Size(max = 256)
    private String currency;
    @Size(max = 256)
    private String exchange;
    private Date ipo;
    private long marketCapitalization;
    @Size(max = 256)
    private String name;
    @Size(max = 256)
    private String phone;
    private double shareOutstanding;
    @Size(max = 256)
    private String ticker;
    @Size(max = 256)
    private String weburl;
    @Size(max = 1000)
    @Column(length = 1000)
    private String logo;
    @Size(max = 256)
    private String finnhubIndustry;
    @JsonBackReference
    @ManyToMany(mappedBy = "companies", fetch = FetchType.LAZY)
    private Set<StockUser> users;
    @JsonIgnore
    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private Set<OHLC> ohlcSet;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return marketCapitalization == company.marketCapitalization &&
                Double.compare(company.shareOutstanding, shareOutstanding) == 0 &&
                symbol.equals(company.symbol) &&
                Objects.equals(country, company.country) &&
                Objects.equals(currency, company.currency) &&
                Objects.equals(exchange, company.exchange) &&
                Objects.equals(ipo, company.ipo) &&
                Objects.equals(name, company.name) &&
                Objects.equals(phone, company.phone) &&
                Objects.equals(ticker, company.ticker) &&
                Objects.equals(weburl, company.weburl) &&
                Objects.equals(logo, company.logo) &&
                Objects.equals(finnhubIndustry, company.finnhubIndustry) &&
                Objects.equals(users, company.users) &&
                Objects.equals(ohlcSet, company.ohlcSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, country, currency, exchange, ipo, marketCapitalization, name, phone, shareOutstanding, ticker, weburl, logo, finnhubIndustry);
    }
}
