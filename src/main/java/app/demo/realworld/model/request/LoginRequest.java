package app.demo.realworld.model.request;

import com.fasterxml.jackson.annotation.JsonRootName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@JsonRootName("user")
public record LoginRequest(
        @NotBlank(message = "can't be empty") @Email(message = "invalid format") String email,
        @NotNull(message = "can't be empty") @Size(min = 6, max = 20) String password) {
}
