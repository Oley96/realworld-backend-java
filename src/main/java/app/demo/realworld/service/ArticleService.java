package app.demo.realworld.service;

import app.demo.realworld.exception.AccessDeniedException;
import app.demo.realworld.exception.EntityNotFoundException;
import app.demo.realworld.model.db.Article;
import app.demo.realworld.model.db.Favorite;
import app.demo.realworld.model.db.Tag;
import app.demo.realworld.model.db.User;
import app.demo.realworld.model.dto.ArticleDto;
import app.demo.realworld.model.dto.ArticlesDto;
import app.demo.realworld.model.dto.FilterDto;
import app.demo.realworld.model.dto.TagDto;
import app.demo.realworld.model.request.CreateArticleRequest;
import app.demo.realworld.model.request.UpdateArticleRequest;
import app.demo.realworld.repository.ArticleRepository;
import app.demo.realworld.repository.FavoriteArticleRepository;
import app.demo.realworld.utils.SlugUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.*;

@Service
@AllArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final FavoriteArticleRepository favoriteArticleRepository;
    private final TagService tagService;

    public Optional<Long> getArticleIdBySlug(String slug) {
        return articleRepository.findArticleIdBySlug(slug);
    }

    public ArticleDto getArticleBySlug(String slug, Optional<User> optionalUser) {
        var userId = optionalUser.map(User::getId).orElse(null);
        return articleRepository.findArticleBySlugAndUserId(slug, SlugUtil.getUniquePart(slug), userId)
                .map(articleDto -> {
                    List<String> tags = tagService.getTagsByArticleId(articleDto.getId());
                    articleDto.setTagList(tags);
                    return articleDto;
                })
                .orElseThrow(() -> new EntityNotFoundException("Article not found"));
    }

    public ArticlesDto getArticles(Optional<User> optionalUser, FilterDto filters) {
        var userId = optionalUser.map(User::getId).orElse(null);
        Pageable pageable = PageRequest.of(filters.offset() / filters.limit(), filters.limit());

        Page<ArticleDto> articleDtos =
                articleRepository.findArticlesByFilters(userId, filters.author(), filters.favorited(), filters.tag(), pageable);

        List<ArticleDto> articleDtoList = articleDtos.getContent();
        addTagsToArticles(articleDtoList);

        return ArticlesDto.of(articleDtoList, articleDtos.getTotalElements());
    }

    public ArticleDto createArticle(CreateArticleRequest request, User user) {
        Article article = Article.of(request, user.getId());
        article.setSlug(SlugUtil.slugifyWithUniquePart(article.getTitle()));

        List<Tag> tags = tagService.handleTags(request.tagList());
        article.getTags().addAll(tags);
        Article saved = articleRepository.save(article);

        return ArticleDto.from(saved, user, tags);
    }

    public ArticlesDto getFeedArticles(Long userId) {
        List<ArticleDto> articlesDto = articleRepository.findFeedArticles(userId);
        addTagsToArticles(articlesDto);

        return ArticlesDto.of(articlesDto, articlesDto.size());
    }

    public ArticleDto updateArticle(String slug, UpdateArticleRequest request, User user) {
        Article savedArticle = articleRepository.findBySlug(slug)
                .map(article -> {
                    handleSlugUpdate(request.title(), article);
                    article.setTitle(request.title());
                    article.setBody(request.body());
                    article.setDescription(request.description());
                    return articleRepository.save(article);
                }).orElseThrow(() -> new EntityNotFoundException("Article not found"));

        long favoriteCount = favoriteArticleRepository.countByArticleId(savedArticle.getId());

        return ArticleDto.from(savedArticle, user, favoriteCount);
    }

    public void deleteArticle(String slug, Long userId) {
        articleRepository.findBySlug(slug)
                .filter(i -> {
                    if (!userId.equals(i.getAuthorId())) {
                        throw new AccessDeniedException("You can delete only own articles");
                    }
                    return true;
                })
                .map(Article::getId)
                .ifPresent(articleRepository::deleteById);
    }

    public ArticleDto favoriteArticle(String slug, User currentUser) {
        articleRepository.findArticleIdBySlug(slug)
                .filter(articleId -> !favoriteArticleRepository.existsByArticleIdAndUserId(articleId, currentUser.getId()))
                .map(articleId -> Favorite.of(currentUser.getId(), articleId))
                .ifPresent(favoriteArticleRepository::save);

        return getArticleBySlug(slug, Optional.of(currentUser));
    }

    public ArticleDto unfavoriteArticle(String slug, User currentUser) {
        articleRepository.findArticleIdBySlug(slug)
                .flatMap(articleId -> favoriteArticleRepository.findByArticleIdAndUserId(articleId, currentUser.getId()))
                .map(Favorite::getId)
                .ifPresent(favoriteArticleRepository::deleteById);

        return getArticleBySlug(slug, Optional.of(currentUser));
    }

    private void addTagsToArticles(List<ArticleDto> articles) {
        Set<Long> articlesIds = articles.stream()
                .map(ArticleDto::getId)
                .collect(toSet());

        Map<Long, List<String>> tagsMap = tagService.getTagsByArticlesIds(articlesIds)
                .stream().collect(groupingBy(TagDto::articleId, mapping(TagDto::tagName, toList())));

        articles.forEach(article ->
                article.setTagList(Optional.ofNullable(tagsMap.get(article.getId())).orElse(List.of())));
    }

    private void handleSlugUpdate(String title, Article article) {
        var uniqueId = SlugUtil.getUniquePart(article.getSlug());
        article.setSlug(SlugUtil.slugifyWithUniquePart(title, uniqueId));
    }
}
