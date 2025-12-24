package app.demo.realworld.repository;

import app.demo.realworld.model.db.Article;
import app.demo.realworld.model.dto.ArticleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("select new app.demo.realworld.model.dto.ArticleDto(" +
            "a, u, (exists(select 1 from Follower f where f.id.followerId = :userId and f.id.followeeId = a.authorId)), " +
            "(select count(*) from Favorite f where f.articleId = a.id), " +
            "(exists(select 1 from Favorite f where f.userId = :userId and f.articleId = a.id))) " +
            "from Article a join User u on a.authorId = u.id " +
            "where (a.slug = :slug or a.slug like %:uniquePart) ")
    Optional<ArticleDto> findArticleBySlugAndUserId(
            @Param("slug") String slug, @Param("uniquePart") String uniquePart, @Param("userId") Long userId);

    @Query("select distinct new app.demo.realworld.model.dto.ArticleDto(" +
            "a, u, (exists(select 1 from Follower f where f.id.followerId = :userId and f.id.followeeId = a.authorId)), " +
            "(select count(*) from Favorite f where f.articleId = a.id), " +
            "(exists(select 1 from Favorite f where f.userId = :userId and f.articleId = a.id))) " +
            "from Article a " +
            "LEFT JOIN User u on a.authorId = u.id " +
            "LEFT JOIN a.tags at " +
            "LEFT JOIN Favorite f ON a.id = f.articleId " +
            "LEFT JOIN Tag t ON t.id = at.id " +
            "WHERE (:author IS NULL OR u.username = :author) " +
            "AND (:tag IS NULL OR t.name = :tag) " +
            "AND (:favoritedBy IS NULL OR f.userId = (select u.id from User u where u.username = :favoritedBy)) ")
    Page<ArticleDto> findArticlesByFilters(
            @Param("userId") Long userId,
            @Param("author") String author,
            @Param("favoritedBy") String favoritedBy,
            @Param("tag") String tag,
            Pageable pageable);

    @Query("select new app.demo.realworld.model.dto.ArticleDto(" +
            "a, u, (exists(select 1 from Follower f where f.id.followerId = :userId and f.id.followeeId = a.authorId)), " +
            "(select count(*) from Favorite f where f.articleId = a.id), " +
            "(exists(select 1 from Favorite f where f.userId = :userId and f.articleId = a.id))) " +
            "from Article a join User u on a.authorId = u.id " +
            "where u.id in (select f.id.followeeId from Follower f where f.id.followerId = :userId)")
    List<ArticleDto> findFeedArticles(@Param("userId") Long userId);

    Optional<Article> findBySlug(String slug);

    @Query("select a.id from Article a where a.slug = :slug")
    Optional<Long> findArticleIdBySlug(@Param("slug") String slug);

    void deleteById(Long id);
}
