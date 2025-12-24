package app.demo.realworld.model.response;

import app.demo.realworld.model.db.Tag;

import java.util.List;

public record TagResponse(List<String> tags) {

    public static TagResponse of(List<Tag> tags) {
        return new TagResponse(tags.stream().map(Tag::getName).toList());
    }
}
