package app.demo.realworld.model.dto;

import app.demo.realworld.model.db.User;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.ALWAYS)
public record ProfileDto(String username, String bio, String image, boolean following) {
    public static ProfileDto of(User user, boolean following) {
        return new ProfileDto(user.getNickname(), user.getBio(), user.getImageUrl(), following);
    }
}
