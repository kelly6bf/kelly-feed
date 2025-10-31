package site.study.post.application.port;

import site.study.post.domain.comment.Comment;

import java.util.Optional;

public interface CommentRepository {

    Comment save(Comment comment);

    Optional<Comment> findById(Long id);
}
