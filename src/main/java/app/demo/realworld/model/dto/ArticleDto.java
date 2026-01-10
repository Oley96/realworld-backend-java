package app.demo.realworld.model.dto;

import app.demo.realworld.model.db.Article;
import app.demo.realworld.model.db.Tag;
import app.demo.realworld.model.db.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
public class ArticleDto {
    @JsonIgnore
    private Long id;
    @JsonIgnore
    private Long authorId;
    private String slug;
    private String title;
    private String description;
    private String body;
    private List<String> tagList;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Instant updatedAt;

    private Boolean favorited = false;
    private Long favoritesCount = 0L;
    private ProfileDto author;
    @JsonIgnore
    private boolean following;

    public ArticleDto(Article article) {
        this.id = article.getId();
        this.slug = article.getSlug();
        this.title = article.getTitle();
        this.description = article.getDescription();
        this.body = article.getBody();
        this.createdAt = article.getCreated();
        this.updatedAt = article.getLastUpdated();
        this.authorId = article.getAuthorId();
    }

    public ArticleDto(Article article, User user, Boolean following, Long favoritesCount, Boolean favorited) {
        this(article);
        this.favorited = favorited;
        this.favoritesCount = favoritesCount;
        this.author = ProfileDto.of(user, following);
    }

    public ArticleDto(Article article, Long favoritesCount, Boolean following, Boolean favorited) {
        this(article);
        this.favoritesCount = favoritesCount;
        this.following = following;
        this.favorited = favorited;
    }

    public static ArticleDto from(Article article, User user, long favoritesCount) {
        ArticleDto articleDto = fromArticle(article);
        articleDto.setAuthor(ProfileDto.of(user, false));
        articleDto.setTagList(article.getTagNames());
        articleDto.setFavoritesCount(favoritesCount);

        return articleDto;
    }

    public static ArticleDto from(Article article, User user, List<Tag> tags) {
        ArticleDto articleDto = fromArticle(article);
        articleDto.setAuthor(ProfileDto.of(user, false));
        articleDto.setTagList(tags.stream().map(Tag::getName).toList());

        return articleDto;
    }

    private static ArticleDto fromArticle(Article article) {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setSlug(article.getSlug());
        articleDto.setTitle(article.getTitle());
        articleDto.setDescription(article.getDescription());
        articleDto.setBody(article.getBody());
        articleDto.setCreatedAt(article.getCreated());
        articleDto.setUpdatedAt(article.getLastUpdated());

        return articleDto;
    }
}
