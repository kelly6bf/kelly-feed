package site.study.user.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.study.user.application.dto.GetUserListResponseDto;
import site.study.user.repository.jpa.entity.UserEntity;

import java.util.List;

public interface JpaUserListQueryRepository extends JpaRepository<UserEntity, Long> {

    @Query(value = "SELECT new site.study.user.application.dto.GetUserListResponseDto(u.name, u.profileImage) "
        + "FROM UserRelationEntity ur "
        + "INNER JOIN UserEntity u ON ur.followerUserId = u.id "
        + "WHERE ur.followingUserId = :userId")
    List<GetUserListResponseDto> getFollowingList(Long userId);

    @Query(value = "SELECT new site.study.user.application.dto.GetUserListResponseDto(u.name, u.profileImage) "
        + "FROM UserRelationEntity ur "
        + "INNER JOIN UserEntity u ON ur.followingUserId = u.id "
        + "WHERE ur.followerUserId = :userId")
    List<GetUserListResponseDto> getFollowerList(Long userId);
}
