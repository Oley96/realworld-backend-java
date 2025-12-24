package app.demo.realworld.repository;

import app.demo.realworld.model.db.User;
import app.demo.realworld.model.dto.ProfileDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    @Query("select new app.demo.realworld.model.dto.ProfileDto(u.username, u.bio, u.imageUrl," +
            "(exists(select 1 from Follower f where f.id.followeeId = u.id and f.id.followerId = :currentUserId))) " +
            "from User u " +
            "where u.username = :username ")
    Optional<ProfileDto> findProfileDtoByUsername(
            @Param("username") String username,
            @Param("currentUserId") Long currentUserId
    );
}