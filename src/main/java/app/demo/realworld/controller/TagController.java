package app.demo.realworld.controller;

import app.demo.realworld.model.db.Tag;
import app.demo.realworld.model.response.TagResponse;
import app.demo.realworld.service.TagService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api")
@AllArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping("/tags")
    public TagResponse getTagsList() {
        List<Tag> tags = tagService.getAll();

        return TagResponse.of(tags);
    }
}
