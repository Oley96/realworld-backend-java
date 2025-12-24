package app.demo.realworld.controller;

import app.demo.realworld.exception.ValidationException;
import app.demo.realworld.model.db.User;
import app.demo.realworld.model.dto.CommentDto;
import app.demo.realworld.model.request.CommentRequest;
import app.demo.realworld.model.response.CommentResponse;
import app.demo.realworld.model.response.CommentsResponse;
import app.demo.realworld.service.CommentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api")
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/articles/{slug}/comments")
    public Object addCommentToArticle(@PathVariable("slug") String slug,
                                      @Valid @RequestBody CommentRequest request, BindingResult bindingResult,
                                      @AuthenticationPrincipal User currentUser) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        CommentDto comment = commentService.addCommentToArticle(request, currentUser, slug);

        return CommentResponse.of(comment);
    }

    @GetMapping("/articles/{slug}/comments")
    public Object getCommentFromArticle(@PathVariable("slug") String slug,
                                        @AuthenticationPrincipal User currentUser) {
        List<CommentDto> comments = commentService.getArticleComments(Optional.ofNullable(currentUser), slug);

        return CommentsResponse.of(comments);
    }

    @DeleteMapping("/articles/{slug}/comments/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable("slug") String slug, @PathVariable("id") Long id,
                                              @AuthenticationPrincipal User currentUser) {
        commentService.deleteCommentFromArticle(currentUser.getId(), slug, id);

        return ResponseEntity.noContent().build();
    }
}
