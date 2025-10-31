package site.study.post.application.port;

import site.study.post.domain.Post;
import site.study.post.domain.comment.Comment;
import site.study.user.domain.User;

public interface LikeRepository {

    boolean checkLike(Post post, User user);

    void like(Post post, User user);

    void unlike(Post post, User user);

    boolean checkLike(Comment comment, User user);

    void like(Comment comment, User user);

    void unlike(Comment comment, User user);
}
