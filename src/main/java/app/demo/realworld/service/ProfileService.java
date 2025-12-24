package app.demo.realworld.service;

import app.demo.realworld.exception.EntityNotFoundException;
import app.demo.realworld.exception.SubscriptionException;
import app.demo.realworld.model.db.User;
import app.demo.realworld.model.dto.ProfileDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserService userService;
    private final FollowerService followerService;

    public ProfileDto getProfile(Optional<User> optionalUser, String username) {
        if (optionalUser.isPresent() && optionalUser.get().getUsername().equals(username)) {
            return ProfileDto.of(optionalUser.get(), false);
        }

        Long currentUserId = optionalUser
                .map(User::getId)
                .orElse(null);

        return userService.findProfileDtoByUsername(username, currentUserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public ProfileDto followUser(User currentUser, String usernameToFollow) {
        return userService.getByUsername(usernameToFollow)
                .filter(userToFollow -> !followerService.checkFollowing(userToFollow.getId(), currentUser.getId()))
                .map(userToFollow -> {
                    followerService.saveFollowing(userToFollow.getId(), currentUser.getId());
                    return ProfileDto.of(userToFollow, true);
                }).orElseThrow(() -> new SubscriptionException("You already subscribed or user not found"));
    }

    public ProfileDto unfollowUser(User currentUser, String usernameToUnfollow) {
        return userService.getByUsername(usernameToUnfollow)
                .map(userToUnfollow -> {
                    followerService.deleteFollowing(userToUnfollow.getId(), currentUser.getId());
                    return ProfileDto.of(userToUnfollow, false);
                }).orElseThrow(() -> new SubscriptionException("User not found"));
    }
}
