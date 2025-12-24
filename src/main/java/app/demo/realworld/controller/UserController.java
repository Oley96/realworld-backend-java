package app.demo.realworld.controller;

import app.demo.realworld.model.db.User;
import app.demo.realworld.model.dto.UserDto;
import app.demo.realworld.model.request.LoginRequest;
import app.demo.realworld.model.request.RegistrationRequest;
import app.demo.realworld.model.request.UpdateUserRequest;
import app.demo.realworld.model.response.AuthUserResponse;
import app.demo.realworld.service.AuthService;
import app.demo.realworld.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/users")
    public AuthUserResponse registerUser(@Valid @RequestBody RegistrationRequest request, BindingResult bindingResult) {
        UserDto userDto = authService.registerUser(request, bindingResult);

        return AuthUserResponse.of(userDto);
    }

    @PostMapping("/users/login")
    public AuthUserResponse loginUser(@Valid @RequestBody LoginRequest request, BindingResult bindingResult) {
        UserDto userDto = authService.loginUser(request, bindingResult);

        return AuthUserResponse.of(userDto);
    }

    @GetMapping("/user")
    public AuthUserResponse getCurrentUser(@RequestHeader(name = "Authorization") String token,
                                           @AuthenticationPrincipal User currentUser) {
        UserDto userDto = UserDto.of(currentUser, token);

        return AuthUserResponse.of(userDto);
    }

    @PutMapping("/user")
    public AuthUserResponse updateUser(@Valid @RequestBody UpdateUserRequest request, BindingResult bindingResult,
                                       @RequestHeader(name = "Authorization") String token,
                                       @AuthenticationPrincipal User currentUser) {
        User updated = userService.update(request, currentUser, bindingResult);
        UserDto userDto = UserDto.of(updated, token);

        return AuthUserResponse.of(userDto);
    }
}
