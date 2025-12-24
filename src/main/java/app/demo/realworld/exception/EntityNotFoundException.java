package app.demo.realworld.exception;

/**
 * @author Volodymyr Oliinyk
 * @since 2024-12-12
 */
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
