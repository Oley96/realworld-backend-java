package app.demo.realworld.model.response;

import app.demo.realworld.model.dto.CommentDto;

import java.util.List;

public record CommentsResponse(List<CommentDto> comments) {
    public static CommentsResponse of(List<CommentDto> comments) {
        return new CommentsResponse(comments);
    }
}
