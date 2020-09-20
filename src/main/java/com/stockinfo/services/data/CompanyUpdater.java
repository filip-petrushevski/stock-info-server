package com.stockinfo.services.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.stockinfo.models.Company;
import com.stockinfo.models.CompanySymbol;
import com.stockinfo.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class CompanyUpdater implements ApplicationRunner {
    private final CompanyRepository companyRepository;

    @Value("${stock-info.app.finnhubKey}")
    private String FINNHUB_API_KEY;

    @Value("https://finnhub.io/api/v1/stock/symbol?exchange=US")
    private String FINNHUB_API_SYMBOLS_URL;

    @Value("https://finnhub.io/api/v1/stock/profile2?")
    private String FINNHUB_API_COMPANY_PROFILE_URL;

    public CompanyUpdater(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(() -> {
            try {
                updateCompanies();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 0, 10, TimeUnit.DAYS); // DELAY SHOULD BE 0
    }

    private void updateCompanies() throws IOException {
        URL url = new URL(FINNHUB_API_SYMBOLS_URL + "&token=" + FINNHUB_API_KEY);
        URLConnection req = url.openConnection();
        req.connect();
        JsonArray jsonResponse = JsonParser.parseReader(new InputStreamReader(req.getInputStream())).getAsJsonArray();
        ObjectMapper objectMapper = new ObjectMapper();
        List<CompanySymbol> companySymbols = objectMapper.readValue(jsonResponse.toString(),
                new TypeReference<List<CompanySymbol>>() {});

        companySymbols.stream()
                .map(companySymbol -> companySymbol.getSymbol())
                .filter(s -> !companyRepository.existsById(s))
                .forEach(s -> {
                    try {
                        Thread.sleep(1200);
                        String profileUrlString = FINNHUB_API_COMPANY_PROFILE_URL +
                                "symbol=" + s + "&token=" + FINNHUB_API_KEY;
                        URL profileReqUrl = new URL(profileUrlString);
                        URLConnection profileReq = profileReqUrl.openConnection();
                        profileReq.connect();
                        JsonObject profileJsonRes = JsonParser.parseReader(
                                new InputStreamReader(profileReq.getInputStream()))
                                .getAsJsonObject();
                        if(profileJsonRes.keySet().isEmpty() ||
                                profileJsonRes.get("logo") == null ||
                                profileJsonRes.get("logo").toString().equals("\"\"") ) {
                            return;
                        }
                        ObjectMapper objectMapper2 = new ObjectMapper();
                        Company company = objectMapper2.readValue(profileJsonRes.toString(), Company.class);
                        company.setSymbol(s);
                        companyRepository.saveAndFlush(company);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });

    }
}
