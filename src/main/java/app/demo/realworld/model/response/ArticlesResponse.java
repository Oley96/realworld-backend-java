package app.demo.realworld.model.response;

import app.demo.realworld.model.dto.ArticleDto;
import app.demo.realworld.model.dto.ArticlesDto;

import java.util.List;

public record ArticlesResponse(List<ArticleDto> articles, long articlesCount) {
    public static ArticlesResponse of(ArticlesDto articlesDto) {
        return new ArticlesResponse(articlesDto.articles(), articlesDto.articlesCount());
    }
}
