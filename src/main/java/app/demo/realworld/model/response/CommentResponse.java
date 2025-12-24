package app.demo.realworld.model.response;

import app.demo.realworld.model.dto.CommentDto;

public record CommentResponse(CommentDto comment) {
    public static CommentResponse of(CommentDto comment) {
        return new CommentResponse(comment);
    }
}
