package site.study.post.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import site.study.post.domain.Post;
import site.study.post.repository.jpa.entity.post.PostEntity;

import java.util.List;

public interface JpaPostRepository extends JpaRepository<PostEntity, Long> {

    @Query("SELECT p.id FROM PostEntity p WHERE p.author.id = :authorId")
    List<Long> findAllPostIdsByAuthorId(Long authorId);

    @Modifying
    @Query("UPDATE PostEntity p "
        + "SET p.likeCount = :#{#post.getLikeCount()} "
        + "WHERE p.id = :#{#post.getId()}")
    void updateLikeCount(Post post);

    @Modifying
    @Query("UPDATE PostEntity p "
        + "SET p.content = :#{#post.getContentText()},"
        + "p.state = :#{#post.getState()},"
        + "p.updDt = now() "
        + "WHERE p.id = :#{#post.getId()}")
    void updatePost(Post post);

    @Modifying
    @Query("UPDATE PostEntity p SET p.commentCount = p.commentCount + 1 WHERE p.id = :postId")
    void increaseCommentCount(Long postId);
}
