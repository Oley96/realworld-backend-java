package app.demo.realworld.model.dto;

import app.demo.realworld.model.db.Comment;
import app.demo.realworld.model.db.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private Instant createdAt;
    private Instant updatedAt;
    private String body;
    private ProfileDto author;

    public CommentDto(Comment comment, User user, Boolean following) {
        this.id = comment.getId();
        this.createdAt = comment.getCreated();
        this.updatedAt = comment.getLastUpdated();
        this.body = comment.getBody();
        this.author = ProfileDto.of(user, following);
    }

    public static CommentDto of(Comment comment, User user, boolean following) {
        CommentDto commentDto = new CommentDto();

        commentDto.setId(comment.getId());
        commentDto.setBody(comment.getBody());
        commentDto.setCreatedAt(comment.getCreated());
        commentDto.setUpdatedAt(comment.getLastUpdated());
        commentDto.setAuthor(ProfileDto.of(user, following));

        return commentDto;
    }
}
