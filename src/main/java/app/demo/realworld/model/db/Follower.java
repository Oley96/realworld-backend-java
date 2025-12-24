package app.demo.realworld.model.db;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Data
@Entity
@Table(name = "follower")
@EntityListeners(AuditingEntityListener.class)
public class Follower {
    @EmbeddedId
    private FollowerFolloweeId id;

    @CreatedDate
    private Instant created;

    public static Follower create(long followeeId, long followerId) {
        Follower follower = new Follower();
        FollowerFolloweeId followerFolloweeId = new FollowerFolloweeId(followeeId, followerId);
        follower.setId(followerFolloweeId);
        return follower;
    }
}
