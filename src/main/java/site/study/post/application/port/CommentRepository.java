package site.study.post.application.port;

import site.study.post.domain.comment.Comment;

public interface CommentRepository {

    Comment save(Comment comment);

    Comment findById(Long id);
}
