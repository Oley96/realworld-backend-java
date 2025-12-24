package app.demo.realworld.model.db;

import app.demo.realworld.model.request.CreateArticleRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

//@Data
@Getter
@Setter
@Entity
@ToString(exclude = "tags")
@Table(name = "article")
@EntityListeners(AuditingEntityListener.class)
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private Instant created;

    @LastModifiedDate
    @Column(name = "last_updated")
    private Instant lastUpdated;

    @Column(name = "author_id")
    private Long authorId;

    private String title;
    private String description;

    @Column(unique = true)
    private String slug;

    private String body;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "article_tag",
            joinColumns = {@JoinColumn(name = "article_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id", referencedColumnName = "id")})
//    @OrderBy("name")
    private List<Tag> tags = new LinkedList<>();

    public static Article of(CreateArticleRequest request, Long authorId) {
        Article article = new Article();
        article.setBody(request.body());
        article.setDescription(request.description());
        article.setTitle(request.title());
        article.setAuthorId(authorId);
        return article;
    }

    public List<String> getTagNames() {
        return tags.stream().map(Tag::getName).toList();
    }
}
