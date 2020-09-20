package com.stockinfo.models.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.stockinfo.models.Article;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;


public class ArticleDeserializer extends StdDeserializer<Article> {
    public ArticleDeserializer() {
        this(null);
    }

    protected ArticleDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Article deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        JsonNode articleNode = p.getCodec().readTree(p);
        Article article = new Article();
        article.setSourceName(articleNode.get("source").get("name").asText());
        article.setContent(articleNode.get("content").asText().split("… \\[")[0] + "...");
        article.setAuthor(articleNode.get("author").asText());
        article.setDescription(articleNode.get("description").asText().split("… \\[")[0] + "...");
        try {
            article.setPublishedAt(df.parse(articleNode.get("publishedAt").asText()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        article.setTitle(articleNode.get("title").asText());
        article.setUrl(articleNode.get("url").asText());
        article.setUrlToImage((articleNode.get("urlToImage").asText()));
        return article;
    }
}
