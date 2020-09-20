package com.stockinfo.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.stockinfo.models.serialization.ArticleDeserializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@JsonDeserialize(using = ArticleDeserializer.class)
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;
    @Size(max = 256)
    private String sourceName;
    @Size(max = 256)
    private String author;
    @Size(max = 256)
    private String title;
    @Size(max = 1000)
    @Column(length = 1000)
    private String description;
    @Size(max = 256)
    private String url;
    @Size(max = 256)
    private String urlToImage;
    private Date publishedAt;
    @Size(max = 256)
    private String content;


}
