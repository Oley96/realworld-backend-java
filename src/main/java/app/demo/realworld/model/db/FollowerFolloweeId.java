package app.demo.realworld.model.db;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class FollowerFolloweeId {

    @Column(name = "follower_id")
    private Long followerId;

    @Column(name = "followee_id")
    private Long followeeId;

    public FollowerFolloweeId() {}

    public FollowerFolloweeId(Long followeeId, Long followerId) {
        this.followerId = followerId;
        this.followeeId = followeeId;
    }
}
