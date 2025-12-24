package app.demo.realworld.validation;

import app.demo.realworld.model.request.RegistrationRequest;
import app.demo.realworld.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SignUpValidator implements Validator {
    private final UserService userService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return RegistrationRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        RegistrationRequest request = (RegistrationRequest) target;
        userService.getByEmail(request.email())
                .ifPresent(i -> errors.rejectValue("email", null, "this email is already taken"));
        userService.getByUsername(request.username())
                .ifPresent(i -> errors.rejectValue("username", null, "this username is already taken"));
    }
}
