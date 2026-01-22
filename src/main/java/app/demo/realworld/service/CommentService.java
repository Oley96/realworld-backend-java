package app.demo.realworld.service;

import app.demo.realworld.exception.AccessDeniedException;
import app.demo.realworld.exception.EntityNotFoundException;
import app.demo.realworld.model.db.Comment;
import app.demo.realworld.model.db.User;
import app.demo.realworld.model.dto.CommentDto;
import app.demo.realworld.model.request.CommentRequest;
import app.demo.realworld.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleService articleService;

    public CommentDto addCommentToArticle(CommentRequest request, User author, String slug) {
        return articleService.getArticleIdBySlug(slug)
                .map(articleId -> Comment.create(request.body(), author.getId(), articleId))
                .map(commentRepository::save)
                .map(comment -> CommentDto.of(comment, author, false))
                .orElseThrow(() -> new EntityNotFoundException("Article does not exist"));
    }

    public List<CommentDto> getArticleComments(Optional<User> optionalUser, String slug) {
        var userid = optionalUser.map(User::getId).orElse(null);
        return articleService.getArticleIdBySlug(slug)
                .map(articleId -> commentRepository.findCommentsDtoByFollowerIdAndArticleId(userid, articleId))
                .orElseThrow(() -> new EntityNotFoundException("Article does not exist"));
    }

    public void deleteCommentFromArticle(Long userId, String slug, Long commentId) {
        Long articleId = articleService.getArticleIdBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException("Article does not exist"));

        Optional<Comment> optionalComment = commentRepository.findByArticleIdAndIdAndAuthorId(articleId, commentId, userId);

        if (optionalComment.isEmpty()) {
            throw new AccessDeniedException("Can't delete comment");
        }

        commentRepository.deleteById(commentId);
    }
}
