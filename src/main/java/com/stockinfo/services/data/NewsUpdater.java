package com.stockinfo.services.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.stockinfo.models.Article;
import com.stockinfo.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class NewsUpdater implements ApplicationRunner {

    @Value("${stock-info.app.newsApiKey}")
    private String NEWS_API_KEY;

    @Value("https://newsapi.org/v2/everything?q=stock")
    private String NEWS_API_URL;

    ArticleRepository articleRepository;

    public NewsUpdater(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(() -> {
            try {
                updateNews();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.HOURS);

    }

    private void updateNews() throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        // Connect to the URL using java's native library
        URL url = new URL(NEWS_API_URL + "&apiKey=" + NEWS_API_KEY);

        URLConnection request = url.openConnection();
        request.connect();
        JsonObject jsonResponse = JsonParser.parseReader(
                new InputStreamReader((InputStream) request.getContent())).getAsJsonObject();

        String stringArticles = jsonResponse.get("articles").toString();
        List<Article> articles = objectMapper.readValue(stringArticles, new TypeReference<List<Article>>() {});
        Article latestArticle = articleRepository.findFirstByOrderByPublishedAtDesc();
        final Date latestArticleDate = latestArticle != null ?
                latestArticle.getPublishedAt() != null ?
                        latestArticle.getPublishedAt() : new Date(Long.MIN_VALUE) : new Date(Long.MIN_VALUE);

        articles.stream()
                .filter(article -> article.getPublishedAt().compareTo(latestArticleDate) > 0)
                .forEach(article -> articleRepository.saveAndFlush(article));
    }
}