package app.demo.realworld.model.response;

public record ErrorResponse(Object errors) {

    public static ErrorResponse of(Object errors) {
        return new ErrorResponse(errors);
    }
}
