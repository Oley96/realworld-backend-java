package app.demo.realworld.repository;

import app.demo.realworld.model.db.Tag;
import app.demo.realworld.model.dto.TagDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query("select t.name from Tag t join t.articles at where at.id = :articleId ")
    List<String> findTagsByArticleId(@Param("articleId") Long articleId);

    @Query("select new app.demo.realworld.model.dto.TagDto(t.name, at.id) " +
            "from Tag t join t.articles at where at.id in :articlesId ")
    List<TagDto> findTagsByArticlesIdIn(@Param("articlesId") Set<Long> articlesId);

    List<Tag> findByNameIn(List<String> name);
}
