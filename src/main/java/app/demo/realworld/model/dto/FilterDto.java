package app.demo.realworld.model.dto;


public record FilterDto(int limit, int offset, String tag, String author, String favorited) {

    public static FilterDto of(int limit, int offset, String tag, String author, String favorited) {
        limit = Math.min(Math.abs(limit), 20); // to prevent query overloading and negative amounts
        return new FilterDto(limit, offset, tag, author, favorited);
    }
}
