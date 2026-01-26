package app.demo.realworld.model.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
    private List<Tag> tags = new LinkedList<>();

    public static Article of(String body, String description, String title, Long authorId) {
        Article article = new Article();
        article.setBody(body);
        article.setDescription(description);
        article.setTitle(title);
        article.setAuthorId(authorId);
        return article;
    }

    public List<String> getTagNames() {
        return tags.stream().map(Tag::getName).toList();
    }
}
