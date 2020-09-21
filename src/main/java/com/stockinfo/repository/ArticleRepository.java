package com.stockinfo.repository;

import com.stockinfo.models.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Article findFirstByOrderByPublishedAtDesc();
    List<Article> findAllByOrderByPublishedAtDesc();
}
