package app.demo.realworld.repository;

import app.demo.realworld.model.db.Comment;
import app.demo.realworld.model.dto.CommentDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select new app.demo.realworld.model.dto.CommentDto( " +
            "c, u, (exists(select 1 from Follower f where f.id.followerId = :userId and f.id.followeeId = c.authorId))) " +
            "from Comment c join User u on c.authorId = u.id " +
            "where c.articleId = :articleId")
    List<CommentDto> findCommentsDtoByFollowerIdAndArticleId(@Param("userId") Long userId, @Param("articleId") Long articleId);

    Optional<Comment> findByArticleIdAndIdAndAuthorId(Long articleId, Long commentId, Long authorId);
}
