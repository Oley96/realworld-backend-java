package app.demo.realworld.model.request;

import com.fasterxml.jackson.annotation.JsonRootName;
import jakarta.validation.constraints.NotBlank;

@JsonRootName("article")
public record UpdateArticleRequest(
        @NotBlank(message = "can't be empty") String title,
        @NotBlank(message = "can't be empty") String description,
        @NotBlank(message = "can't be empty") String body) {
}
