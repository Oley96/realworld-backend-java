package app.demo.realworld.model.response;

import app.demo.realworld.model.dto.ArticleDto;

public record ArticleResponse(ArticleDto article) {
    public static ArticleResponse of(ArticleDto article) {
        return new ArticleResponse(article);
    }
}

