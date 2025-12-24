package app.demo.realworld.model.request;

import com.fasterxml.jackson.annotation.JsonRootName;
import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.URL;

@JsonRootName("user")
public record UpdateUserRequest(@Email String email, String password, String username, String bio, @URL String image) {
}
