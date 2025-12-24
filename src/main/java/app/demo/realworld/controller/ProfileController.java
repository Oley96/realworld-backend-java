package app.demo.realworld.controller;

import app.demo.realworld.model.db.User;
import app.demo.realworld.model.dto.ProfileDto;
import app.demo.realworld.model.response.ProfileResponse;
import app.demo.realworld.service.ProfileService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api")
@AllArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("/profiles/{username}")
    public ProfileResponse getProfileInfo(@PathVariable String username,
                                          @AuthenticationPrincipal User optionalUser) {
        ProfileDto profile = profileService.getProfile(Optional.ofNullable(optionalUser), username);

        return ProfileResponse.of(profile);
    }

    @PostMapping("/profiles/{username}/follow")
    public ProfileResponse followToUser(@PathVariable String username,
                                        @AuthenticationPrincipal User currentUser) {
        ProfileDto profile = profileService.followUser(currentUser, username);

        return ProfileResponse.of(profile);
    }

    @DeleteMapping("/profiles/{username}/follow")
    public ProfileResponse unfollowFromUser(@PathVariable String username,
                                            @AuthenticationPrincipal User currentUser) {
        ProfileDto profile = profileService.unfollowUser(currentUser, username);

        return ProfileResponse.of(profile);
    }
}
