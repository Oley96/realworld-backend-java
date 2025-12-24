package app.demo.realworld.repository;

import app.demo.realworld.model.db.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteArticleRepository extends JpaRepository<Favorite, Long> {
    boolean existsByArticleIdAndUserId(Long articleId, Long userId);
    Optional<Favorite> findByArticleIdAndUserId(Long articleId, Long userId);
    int countByArticleId(Long articleId);
}
