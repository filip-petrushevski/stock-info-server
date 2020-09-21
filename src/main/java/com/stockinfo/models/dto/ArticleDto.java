package com.stockinfo.models.dto;

import com.stockinfo.models.Article;
import lombok.Data;

import java.util.Date;

@Data
public class ArticleDto {
    private Long id;
    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private Date publishedAt;

    public ArticleDto(Article article) {
        this.id = article.getId();
        this.author = article.getAuthor();
        this.title = article.getTitle();
        this.description = article.getDescription();
        this.url = article.getUrl();
        this.urlToImage = article.getUrlToImage();
        this.publishedAt = article.getPublishedAt();
    }
}