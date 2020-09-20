package com.stockinfo.services.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.stockinfo.models.Company;
import com.stockinfo.models.OHLC;
import com.stockinfo.repository.CompanyRepository;
import com.stockinfo.repository.OHLCRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class OHLCUpdater implements ApplicationRunner {
    private final CompanyRepository companyRepository;
    private final OHLCRepository ohlcRepository;

    @Value("${stock-info.app.finnhubKey}")
    private String FINNHUB_API_KEY;

    @Value("https://finnhub.io/api/v1/stock/candle?resolution=D&")
    private String FINNHUB_API_CANDLE_URL;

    public OHLCUpdater(CompanyRepository companyRepository,
                       OHLCRepository ohlcRepository) {
        this.companyRepository = companyRepository;
        this.ohlcRepository = ohlcRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(() -> {
            updateOHLC();
        }, 3, 24, TimeUnit.HOURS); // DELAY SHOULD BE 3
    }

    private void updateOHLC() {
        Long from = Instant.now().minus(40, ChronoUnit.DAYS).getEpochSecond();
        Long to = Instant.now().getEpochSecond();
        String fromAndTo = "&from=" + from + "&to=" + to;
        this.companyRepository.findAll().stream()
                .map(Company::getSymbol)
                .forEach(s -> {
                    try {
                        Thread.sleep(1200);
                        URL url = new URL(FINNHUB_API_CANDLE_URL + "symbol=" + s + fromAndTo +
                                "&token=" + FINNHUB_API_KEY);
                        URLConnection req = null;
                        req = url.openConnection();
                        req.connect();
                        JsonObject jsonRes = JsonParser.parseReader(new InputStreamReader(req.getInputStream()))
                                .getAsJsonObject();
                        JsonArray o = jsonRes.get("o").getAsJsonArray();
                        JsonArray h = jsonRes.get("h").getAsJsonArray();
                        JsonArray l = jsonRes.get("l").getAsJsonArray();
                        JsonArray c = jsonRes.get("c").getAsJsonArray();
                        JsonArray v = jsonRes.get("v").getAsJsonArray();
                        JsonArray t = jsonRes.get("t").getAsJsonArray();
                        Company company = companyRepository.getOne(s);
                        if(o != null && o.size() > 15) {
                            ohlcRepository.findAllByCompanySymbol(s).stream()
                                    .forEach(ohlc -> ohlcRepository.deleteById(ohlc.getId()));
                        }
                        for (int i = 0; i < o.size(); i++) {
                            ohlcRepository.saveAndFlush(new OHLC(Date.from(Instant.ofEpochSecond(t.get(i).getAsLong())),
                                    company,
                                    o.get(i).getAsDouble(),
                                    h.get(i).getAsDouble(),
                                    l.get(i).getAsDouble(),
                                    c.get(i).getAsDouble(),
                                    v.get(i).getAsLong()));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
    }
}
