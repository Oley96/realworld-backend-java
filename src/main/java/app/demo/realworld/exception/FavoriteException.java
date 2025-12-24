package app.demo.realworld.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Volodymyr Oliinyk
 * @since 2024-12-12
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FavoriteException extends RuntimeException {
    public FavoriteException(String message) {
        super(message);
    }
}
