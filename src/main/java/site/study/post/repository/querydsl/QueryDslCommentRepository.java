package site.study.post.repository.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import site.study.post.application.dto.GetCommentContentResponseDto;
import site.study.post.repository.jpa.entity.comment.QCommentEntity;
import site.study.post.repository.jpa.entity.like.QLikeEntity;
import site.study.user.repository.jpa.entity.QUserEntity;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QueryDslCommentRepository {

    private static final QCommentEntity commentEntity = QCommentEntity.commentEntity;
    private static final QUserEntity userEntity = QUserEntity.userEntity;
    private static final QLikeEntity likeEntity = QLikeEntity.likeEntity;

    private final JPAQueryFactory jpaQueryFactory;

    public List<GetCommentContentResponseDto> getCommentContents(final Long postId, final Long userId, Long lastContentId) {
        return jpaQueryFactory
            .select(
                Projections.fields(
                    GetCommentContentResponseDto.class,
                    commentEntity.id.as("id"),
                    commentEntity.content.as("content"),
                    userEntity.id.as("userId"),
                    userEntity.name.as("userName"),
                    userEntity.profileImage.as("userProfileImage"),
                    commentEntity.regDt.as("createdAt"),
                    commentEntity.updDt.as("updatedAt"),
                    commentEntity.likeCount.as("likeCount"),
                    likeEntity.isNotNull().as("isLikedByMe")
                )
            )
            .from(commentEntity)
            .join(userEntity).on(userEntity.id.eq(commentEntity.author.id))
            .leftJoin(likeEntity).on(hasLike(userId))
            .where(
                commentEntity.post.id.eq(postId),
                hasLastData(lastContentId)
            )
            .orderBy(commentEntity.id.asc())
            .limit(30)
            .fetch();
    }

    private BooleanExpression hasLastData(Long lastId) {
        if (lastId == null) {
            return null;
        }

        return commentEntity.id.lt(lastId);
    }

    private BooleanExpression hasLike(Long userId) {
        if (userId == null) {
            return null;
        }

        return commentEntity.id
            .eq(likeEntity.id.targetId)
            .and(likeEntity.id.targetType.eq("COMMENT"))
            .and(likeEntity.id.userId.eq(userId));
    }
}
