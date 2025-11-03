package site.study.post.repository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import site.study.post.application.port.CommentRepository;
import site.study.post.domain.comment.Comment;
import site.study.post.repository.jpa.JpaCommentRepository;
import site.study.post.repository.jpa.JpaPostRepository;
import site.study.post.repository.jpa.entity.comment.CommentEntity;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {

    private final JpaCommentRepository jpaCommentRepository;
    private final JpaPostRepository jpaPostRepository;

    @Override
    public Comment findById(Long id) {
        CommentEntity entity = jpaCommentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        return entity.toComment();
    }

    @Override
    @Transactional
    public Comment save(Comment comment) {
        if (comment.getId() != null) {
            jpaCommentRepository.updateComment(comment);
            return comment;
        }

        CommentEntity entity = jpaCommentRepository.save(new CommentEntity(comment));
        jpaPostRepository.increaseCommentCount(entity.getPost().getId());
        return entity.toComment();
    }
}
