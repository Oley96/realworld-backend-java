package app.demo.realworld.exception;

import app.demo.realworld.model.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(UNPROCESSABLE_ENTITY) // front end requires 422 code
    @ResponseBody
    @ExceptionHandler(ValidationException.class)
    public ErrorResponse methodArgumentNotValidException(ValidationException ex) {
        Map<String, List<String>> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            var fieldName = ((FieldError) error).getField();
            var errorMessage = List.of(Objects.requireNonNull(error.getDefaultMessage()));
            errors.put(fieldName, errorMessage);
        });
        return ErrorResponse.of(errors);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> entityNotFoundException(EntityNotFoundException ex) {
        return  ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(SubscriptionException.class)
    public ResponseEntity<Void> subscriptionException(SubscriptionException ex) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Void> accessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(403).build();
    }
}
