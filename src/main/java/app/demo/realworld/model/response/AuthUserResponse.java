package app.demo.realworld.model.response;

import app.demo.realworld.model.dto.UserDto;

public record AuthUserResponse(UserDto user) {

    public static AuthUserResponse of(UserDto userDto) {
        return new AuthUserResponse(userDto);
    }
}
