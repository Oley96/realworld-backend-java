package app.demo.realworld.model.request;

import com.fasterxml.jackson.annotation.JsonRootName;
import jakarta.validation.constraints.NotBlank;

@JsonRootName("comment")
public record CommentRequest(
        @NotBlank(message = "can't be empty") String body
) {

    public static CommentRequest of(String body) {
        return new CommentRequest(body);
    }
}
