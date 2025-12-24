package app.demo.realworld.controller;

import app.demo.realworld.exception.ValidationException;
import app.demo.realworld.model.db.User;
import app.demo.realworld.model.dto.ArticleDto;
import app.demo.realworld.model.dto.ArticlesDto;
import app.demo.realworld.model.dto.FilterDto;
import app.demo.realworld.model.request.CreateArticleRequest;
import app.demo.realworld.model.request.UpdateArticleRequest;
import app.demo.realworld.model.response.ArticleResponse;
import app.demo.realworld.model.response.ArticlesResponse;
import app.demo.realworld.service.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/articles/{slug}")
    public ArticleResponse getSingleArticle(@PathVariable(name = "slug") String slug, @AuthenticationPrincipal User user) {
        ArticleDto articleDto = articleService.getArticleBySlug(slug, Optional.ofNullable(user));

        return ArticleResponse.of(articleDto);
    }

    @GetMapping("/articles")
    public ArticlesResponse getArticles(
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String favorited,
            @AuthenticationPrincipal User user) {
        FilterDto filters = FilterDto.of(limit, offset, tag, author, favorited);
        ArticlesDto articlesDto = articleService.getArticles(Optional.ofNullable(user), filters);

        return ArticlesResponse.of(articlesDto);
    }

    @GetMapping("/articles/feed")
    public ArticlesResponse getFeedArticles(@AuthenticationPrincipal User currentUser) {
        ArticlesDto articlesDto = articleService.getFeedArticles(currentUser.getId());

        return ArticlesResponse.of(articlesDto);
    }

    @PostMapping("/articles")
    public ArticleResponse createArticle(@Valid @RequestBody CreateArticleRequest request, BindingResult bindingResult,
                                         @AuthenticationPrincipal User currentUser) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        ArticleDto articleDto = articleService.createArticle(request, currentUser);

        return ArticleResponse.of(articleDto);
    }

    @PutMapping("/articles/{slug}")
    public ArticleResponse updateArticle(@PathVariable String slug,
                                         @Valid @RequestBody UpdateArticleRequest request, BindingResult bindingResult,
                                         @AuthenticationPrincipal User currentUser) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        ArticleDto articleDto = articleService.updateArticle(slug, request, currentUser);

        return ArticleResponse.of(articleDto);
    }

    @DeleteMapping("/articles/{slug}")
    public ResponseEntity<Void> updateArticle(@PathVariable String slug,
                                              @AuthenticationPrincipal User currentUser) {
        articleService.deleteArticle(slug, currentUser.getId());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/articles/{slug}/favorite")
    public ArticleResponse favoriteArticle(@PathVariable String slug,
                                           @AuthenticationPrincipal User currentUser) {
        ArticleDto articleDto = articleService.favoriteArticle(slug, currentUser);

        return ArticleResponse.of(articleDto);
    }

    @DeleteMapping("/articles/{slug}/favorite")
    public ArticleResponse unfavoriteArticle(@PathVariable String slug,
                                             @AuthenticationPrincipal User currentUser) {
        ArticleDto articleDto = articleService.unfavoriteArticle(slug, currentUser);

        return ArticleResponse.of(articleDto);
    }
}
