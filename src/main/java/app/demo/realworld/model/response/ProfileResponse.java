package app.demo.realworld.model.response;

import app.demo.realworld.model.dto.ProfileDto;

public record ProfileResponse(ProfileDto profile) {
    public static ProfileResponse of(ProfileDto profileDto) {
        return new ProfileResponse(profileDto);
    }
}
