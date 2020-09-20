package com.stockinfo.services.impl;

import com.stockinfo.models.Article;
import com.stockinfo.models.dto.ArticleDto;
import com.stockinfo.repository.ArticleRepository;
import com.stockinfo.services.ArticleService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public List<ArticleDto> getAllArticles() {
        return articleRepository.findAllByOrderByPublishedAtDesc()
                .stream()
                .map(article -> new ArticleDto(article))
                .collect(Collectors.toList());
    }
}
