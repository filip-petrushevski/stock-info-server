package com.stockinfo.repository;

import com.stockinfo.models.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
//    @Query(value = "SELECT new com.stockinfo.models.Article (a) FROM Article a WHERE a.publishedAt = (SELECT MAX(a.pubilishedAt) FROM Article a)")
//    Article findLastestArticle();
    Article findFirstByOrderByPublishedAtDesc();
    List<Article> findAllByOrderByPublishedAtDesc();
}
