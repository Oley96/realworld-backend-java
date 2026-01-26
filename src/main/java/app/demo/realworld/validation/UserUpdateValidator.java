package app.demo.realworld.validation;

import app.demo.realworld.model.db.User;
import app.demo.realworld.model.request.UpdateUserRequest;
import app.demo.realworld.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserUpdateValidator implements Validator {
    private final UserRepository userRepository;

    @Setter
    private User currentUser;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return UpdateUserRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        UpdateUserRequest request = (UpdateUserRequest) target;

        Optional.ofNullable(request.email())
                .filter(email -> {
                    if (StringUtils.isBlank(email)) {
                        errors.rejectValue("email", null, "email should not be blank");
                        return false;
                    }
                    return true;
                })
                .filter(email -> !email.equals(currentUser.getEmail()))
                .flatMap(userRepository::findByEmail)
                .ifPresent(email -> errors.rejectValue("email", null, "email is already taken"));

        Optional.ofNullable(request.username())
                .filter(username -> {
                    if (StringUtils.isBlank(username)) {
                        errors.rejectValue("username", null, "username should not be blank");
                        return false;
                    }
                    return true;
                })
                .filter(username -> !username.equals(currentUser.getNickname()))
                .flatMap(userRepository::findByUsername)
                .ifPresent(user -> errors.rejectValue("username", null, "username is already taken"));


        Optional.ofNullable(request.password())
                .filter(StringUtils::isNotBlank)
                .filter(password -> password.length() < 6 || password.length() > 20)
                .ifPresent(password -> errors.rejectValue("password", null, "password should be between 6 and 20 characters"));
    }
}
