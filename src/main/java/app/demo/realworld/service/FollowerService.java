package app.demo.realworld.service;

import app.demo.realworld.model.db.Follower;
import app.demo.realworld.repository.FollowerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FollowerService {
    private final FollowerRepository followerRepository;

    public boolean checkFollowing(long followeeId, long followerId) {
        return followerRepository.existsBy(followeeId, followerId);
    }

    public void deleteFollowing(long followeeId, long followerId) {
        followerRepository.deleteBy(followeeId, followerId);
    }

    public void saveFollowing(long followeeId, long followerId) {
        followerRepository.save(Follower.create(followeeId, followerId));
    }
}
