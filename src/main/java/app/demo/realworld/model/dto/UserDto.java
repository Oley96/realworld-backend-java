package app.demo.realworld.model.dto;

import app.demo.realworld.model.db.User;
import com.fasterxml.jackson.annotation.JsonInclude;

import static app.demo.realworld.security.JwtAuthenticationFilter.TOKEN_PREFIX;

@JsonInclude(JsonInclude.Include.ALWAYS)
public record UserDto(String email, String token, String username, String bio, String image) {

    public static UserDto of(User user, String token) {
        token = token.replace(TOKEN_PREFIX, "");
        return new UserDto(user.getEmail(), token, user.getUserName(), user.getBio(), user.getImageUrl());
    }
}
