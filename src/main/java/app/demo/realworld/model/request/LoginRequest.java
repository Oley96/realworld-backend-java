package app.demo.realworld.model.request;

import com.fasterxml.jackson.annotation.JsonRootName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonRootName("user")
public record LoginRequest(
        @NotBlank(message = "can't be empty") @NotNull(message = "can't be null") @Email(message = "invalid format") String email,
        @NotBlank(message = "can't be empty") @NotNull(message = "can't be null") String password) {
}
