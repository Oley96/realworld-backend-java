package app.demo.realworld.service;

import app.demo.realworld.exception.ValidationException;
import app.demo.realworld.model.db.User;
import app.demo.realworld.model.dto.ProfileDto;
import app.demo.realworld.model.request.UpdateUserRequest;
import app.demo.realworld.repository.UserRepository;
import app.demo.realworld.validation.UserUpdateValidator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserUpdateValidator userUpdateValidator;

    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<ProfileDto> findProfileDtoByUsername(String username, Long currentUserId) {
        return userRepository.findProfileDtoByUsername(username, currentUserId);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User update(UpdateUserRequest request, User user, BindingResult bindingResult) {
        userUpdateValidator.setCurrentUser(user);
        userUpdateValidator.validate(request, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        Optional.ofNullable(request.password())
                .filter(StringUtils::isNotEmpty)
                .filter(pass -> !passwordEncoder.matches(pass, user.getPassword()))
                .ifPresent(pass -> user.setPassword(passwordEncoder.encode(pass)));

        Optional.ofNullable(request.email()).ifPresent(user::setEmail);
        Optional.ofNullable(request.username()).ifPresent(user::setUsername);
        Optional.ofNullable(request.bio()).ifPresent(user::setBio);
        Optional.ofNullable(request.image()).ifPresent(user::setImageUrl);

        return userRepository.save(user);
    }
}
