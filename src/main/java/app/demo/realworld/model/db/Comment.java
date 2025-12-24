package app.demo.realworld.model.db;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Data
@Entity
@Table(name = "comment")
@EntityListeners(AuditingEntityListener.class)
public class Comment {
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

    @Column(name = "article_id")
    private Long articleId;

    private String body;

    public static Comment create(String body, Long authorId, Long articleId) {
        Comment comment = new Comment();
        comment.setAuthorId(authorId);
        comment.setArticleId(articleId);
        comment.setBody(body);
        return comment;
    }
}
