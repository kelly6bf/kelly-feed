package site.study.post.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import site.study.post.domain.comment.Comment;
import site.study.post.repository.jpa.entity.comment.CommentEntity;

public interface JpaCommentRepository extends JpaRepository<CommentEntity, Long> {

    @Modifying
    @Query("UPDATE CommentEntity c "
        + "SET c.likeCount = :#{#comment.getLikeCount()} "
        + "WHERE c.id = :#{#comment.getId()}")
    void updateLikeCount(Comment comment);

    @Modifying
    @Query("UPDATE CommentEntity c "
        + "SET c.content = :#{#comment.getContentText()},"
        + "c.updDt = now() "
        + "WHERE c.id = :#{#comment.getId()}")
    void updateComment(Comment comment);
}
