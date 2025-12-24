package app.demo.realworld.service;

import app.demo.realworld.exception.ValidationException;
import app.demo.realworld.model.db.User;
import app.demo.realworld.model.dto.UserDto;
import app.demo.realworld.model.request.LoginRequest;
import app.demo.realworld.model.request.RegistrationRequest;
import app.demo.realworld.security.jwt.JwtTokenProvider;
import app.demo.realworld.validation.LoginValidator;
import app.demo.realworld.validation.SignUpValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final SignUpValidator signUpValidator;
    private final LoginValidator loginValidator;

    @Value("${image.default.url}")
    private String imageUrl;

    public UserDto registerUser(RegistrationRequest request, BindingResult bindingResult) {
        signUpValidator.validate(request, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        User user = new User();
        user.setEmail(request.email());
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setImageUrl(imageUrl);


        User saved = userService.save(user);

        return UserDto.of(saved, jwtTokenProvider.issueToken(user.getEmail()));
    }

    public UserDto loginUser(LoginRequest request, BindingResult bindingResult) {
        loginValidator.validate(request, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        return userService.getByEmail(request.email())
                .map(user -> UserDto.of(user, jwtTokenProvider.issueToken(user.getEmail())))
                .orElse(null);
    }
}
