package app.demo.realworld.article;

import app.demo.realworld.exception.EntityNotFoundException;
import app.demo.realworld.model.db.User;
import app.demo.realworld.model.dto.ArticleDto;
import app.demo.realworld.repository.ArticleRepository;
import app.demo.realworld.service.ArticleService;
import app.demo.realworld.service.TagService;
import app.demo.realworld.utils.SlugUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Volodymyr Oliinyk
 * @since 2025-09-27
 */
@ExtendWith(MockitoExtension.class)
public class ArticleServiceTests {

    @InjectMocks
    private ArticleService underTest;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private TagService tagService;

    @Test
    void testShouldGetArticleIdBySlug() {
        //arrange
        var slug = "testSlug";
        var expected = 1L;
        Mockito.when(articleRepository.findArticleIdBySlug("testSlug")).thenReturn(Optional.of(expected));

        //act
        Optional<Long> result = underTest.getArticleIdBySlug(slug);

        //assert
        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
        verify(articleRepository, times(2)).findArticleIdBySlug(slug);
    }

    @Test
    void testShouldNotGetArticleIdBySlug() {
        //arrange
        var slug = "testSlug";
        Mockito.when(articleRepository.findArticleIdBySlug("testSlug")).thenReturn(Optional.empty());

        //act
        Optional<Long> result = underTest.getArticleIdBySlug(slug);

        //assert
        assertTrue(result.isEmpty());
        verify(articleRepository, times(1)).findArticleIdBySlug(slug);
    }

    @Test
    void testShouldGetArticleBySlugWithEmptyTagList() {
        //arrange
        var slug = "testSlug";

        User user = Mockito.mock(User.class);
        user.setId(1L);

        ArticleDto article = Mockito.mock(ArticleDto.class);
        article.setId(1L);
        Mockito.when(articleRepository.findArticleBySlugAndUserId(slug, SlugUtil.getUniquePart(slug), user.getId()))
                .thenReturn(Optional.of(article));
        Mockito.when(tagService.getTagsByArticleId(article.getId())).thenReturn(Collections.emptyList());

        //act
        ArticleDto result = underTest.getArticleBySlug(slug, Optional.of(user));

        //assert
        verify(articleRepository, times(1))
                .findArticleBySlugAndUserId(slug, SlugUtil.getUniquePart(slug), user.getId());
        assertTrue(result.getTagList().isEmpty());
        assertEquals(article.getId(), result.getId());
        verify(tagService).getTagsByArticleId(result.getId());
    }

    @Test
    void testShouldGetArticleBySlugWithTagListAndWithUser() {
        //arrange
        var slug = "testSlug";

        User user = Mockito.mock(User.class);
        user.setId(1L);

        ArticleDto article = Mockito.mock(ArticleDto.class);
        article.setId(1L);

        List<String> expectedTagList = List.of("tag1", "tag2");

        Mockito.when(articleRepository.findArticleBySlugAndUserId(slug, SlugUtil.getUniquePart(slug), user.getId()))
                .thenReturn(Optional.of(article));
        Mockito.when(tagService.getTagsByArticleId(article.getId())).thenReturn(expectedTagList);

        //act
        ArticleDto result = underTest.getArticleBySlug(slug, Optional.of(user));

        //assert
        verify(articleRepository, times(1))
                .findArticleBySlugAndUserId(slug, SlugUtil.getUniquePart(slug), user.getId());
        assertEquals(article.getId(), result.getId());
        assertEquals(article.getTagList(), result.getTagList());
        verify(tagService).getTagsByArticleId(result.getId());
    }

    @Test
    void testShouldGetArticleBySlugWithTagListAndWithoutUser() {
        //arrange
        var slug = "testSlug";

        ArticleDto article = Mockito.mock(ArticleDto.class);
        article.setId(1L);

        List<String> expectedTagList = List.of("tag1", "tag2");

        Mockito.when(articleRepository.findArticleBySlugAndUserId(slug, SlugUtil.getUniquePart(slug), null))
                .thenReturn(Optional.of(article));
        Mockito.when(tagService.getTagsByArticleId(article.getId())).thenReturn(expectedTagList);

        //act
        ArticleDto result = underTest.getArticleBySlug(slug, Optional.empty());

        //assert
        verify(articleRepository, times(1))
                .findArticleBySlugAndUserId(slug, SlugUtil.getUniquePart(slug), null);
        assertEquals(article.getId(), result.getId());
        assertEquals(article.getTagList(), result.getTagList());
        verify(tagService).getTagsByArticleId(result.getId());
    }

    @Test
    void testShouldThrowWhenArticleNotFound() {
        //arrange
        var slug = "testSlug";

        ArticleDto article = Mockito.mock(ArticleDto.class);
        article.setId(1L);

        User user = Mockito.mock(User.class);
        user.setId(1L);

        Mockito.when(articleRepository.findArticleBySlugAndUserId(slug, SlugUtil.getUniquePart(slug), user.getId()))
                .thenReturn(Optional.empty());

        //act + asset
        assertThrowsExactly(EntityNotFoundException.class,
                () -> underTest.getArticleBySlug(slug, Optional.of(user)), "Article not found");
        verifyNoInteractions(tagService);
    }
}
