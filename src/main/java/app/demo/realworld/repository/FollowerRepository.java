package app.demo.realworld.repository;

import app.demo.realworld.model.db.Follower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, Integer> {
    @Query("select exists(select 1 from Follower f where f.id.followeeId = :followeeId and f.id.followerId = :followerId) ")
    boolean existsBy(@Param("followeeId") Long followeeId, @Param("followerId") Long followerId);

    @Modifying
    @Transactional
    @Query("delete from Follower f where f.id.followeeId = :followeeId and f.id.followerId = :followerId ")
    void deleteBy(@Param("followeeId") Long followeeId, @Param("followerId") Long followerId);
}
