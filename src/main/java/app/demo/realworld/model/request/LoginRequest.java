package app.demo.realworld.model.request;

import com.fasterxml.jackson.annotation.JsonRootName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@JsonRootName("user")
public record LoginRequest(
        @NotBlank(message = "can't be empty") @Email(message = "invalid format") String email,
        @NotBlank(message = "can't be empty") String password) {
}
