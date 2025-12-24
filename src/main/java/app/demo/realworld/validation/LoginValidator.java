package app.demo.realworld.validation;

import app.demo.realworld.model.db.User;
import app.demo.realworld.model.request.LoginRequest;
import app.demo.realworld.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LoginValidator implements Validator {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return LoginRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        LoginRequest request = (LoginRequest) target;
        Optional<User> optionalUser = userService.getByEmail(request.email());
        if (optionalUser.isEmpty()) {
            errors.rejectValue("email", null, "invalid email or password");
            return;
        }

        optionalUser.map(User::getPassword)
                .filter(password -> !passwordEncoder.matches(request.password(), password))
                .ifPresent(i -> errors.rejectValue("password", null, "invalid email or password"));
    }
}
