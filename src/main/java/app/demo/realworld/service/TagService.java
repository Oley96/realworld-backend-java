package app.demo.realworld.service;

import app.demo.realworld.model.db.Tag;
import app.demo.realworld.model.dto.TagDto;
import app.demo.realworld.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public List<String> getTagsByArticleId(Long articleId) {
        return tagRepository.findTagsByArticleId(articleId);
    }

    public List<TagDto> getTagsByArticlesIds(Set<Long> articleIds) {
        return tagRepository.findTagsByArticlesIdIn(articleIds);
    }

    public List<Tag> handleTags(List<String> tags) {
        List<Tag> existingTags = tagRepository.findByNameIn(tags);

        Set<String> existingTagNames = existingTags.stream()
                .map(Tag::getName)
                .collect(Collectors.toSet());

        List<Tag> toSave = tags.stream()
                .filter(tagName -> !existingTagNames.contains(tagName))
                .map(Tag::of)
                .toList();

        List<Tag> savedTags = tagRepository.saveAll(toSave);
        existingTags.addAll(savedTags);

        return existingTags;
    }

    public List<Tag> getAll() {
        return tagRepository.findAll();
    }
}
