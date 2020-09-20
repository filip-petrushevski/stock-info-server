package com.stockinfo.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OHLC {
    public OHLC(Date date, Company company, Double open, Double high, Double low, Double close, Long volume) {
        this.date = date;
        this.company = company;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.DATE)
    private Date date;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;
    private Double open;
    private Double high;
    private Double low;
    private Double close;
    private Long volume;
}