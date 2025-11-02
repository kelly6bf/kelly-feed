package site.study.post.repository;

import site.study.post.application.port.CommentRepository;
import site.study.post.domain.comment.Comment;

import java.util.HashMap;
import java.util.Map;

public class FakeCommentRepository implements CommentRepository {

    private final Map<Long, Comment> store = new HashMap<>();

    @Override
    public Comment findById(Long id) {
        return store.get(id);
    }

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() != null) {
            store.put(comment.getId(), comment);
            return comment;
        }

        long id = store.size() + 1;
        Comment newComment = new Comment(id, comment.getPost(), comment.getAuthor(), comment.getContent());
        store.put(id, newComment);
        return newComment;
    }
}
