package com.stockinfo.services;


import com.stockinfo.models.dto.ArticleDto;

import java.util.List;

public interface ArticleService {
    List<ArticleDto> getAllArticles();
}
