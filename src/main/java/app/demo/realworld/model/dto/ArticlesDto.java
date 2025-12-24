package app.demo.realworld.model.dto;


import java.util.List;

public record ArticlesDto(List<ArticleDto> articles, long articlesCount) {

    public static ArticlesDto of(List<ArticleDto> articles, long articlesCount) {
        articles.forEach(item -> item.setBody(null));
        return new ArticlesDto(articles, articlesCount);
    }
}
