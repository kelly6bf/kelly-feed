package site.study.user.repository.jpa;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import site.study.user.application.dto.GetUserListResponseDto;
import site.study.user.repository.jpa.entity.QUserEntity;
import site.study.user.repository.jpa.entity.QUserRelationEntity;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaUserListPagingQueryRepository {

    private final JPAQueryFactory queryFactory;
    private static final QUserEntity userEntity = QUserEntity.userEntity;
    private static final QUserRelationEntity relationEntity = QUserRelationEntity.userRelationEntity;

    public List<GetUserListResponseDto> getFollowerList(Long userId, Long lastFollowerId) {
        return queryFactory
            .select(
                Projections.fields(
                    GetUserListResponseDto.class,
                    userEntity.name.as("name"),
                    userEntity.profileImage.as("profileImage")
                )
            )
            .from(relationEntity)
            .join(userEntity).on(relationEntity.followingUserId.eq(userEntity.id))
            .where(
                relationEntity.followerUserId.eq(userId),
                hasLastData(lastFollowerId)
            )
            .orderBy(userEntity.id.desc())
            .limit(100L)
            .fetch();
    }

    private BooleanExpression hasLastData(Long lastId) {
        if (lastId == null) {
            return null;
        }

        return userEntity.id.lt(lastId);
    }
}
