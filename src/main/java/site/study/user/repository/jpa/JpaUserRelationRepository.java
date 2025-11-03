package site.study.user.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.study.user.repository.jpa.entity.UserRelationEntity;
import site.study.user.repository.jpa.entity.UserRelationId;

import java.util.List;

public interface JpaUserRelationRepository extends JpaRepository<UserRelationEntity, UserRelationId> {

    @Query("SELECT u.followingUserId FROM UserRelationEntity u WHERE u.followerUserId = :userId")
    List<Long> findFollowers(Long userId);
}
