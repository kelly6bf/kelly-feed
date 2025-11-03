package site.study.post.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import site.study.post.application.port.LikeRepository;
import site.study.post.domain.Post;
import site.study.post.domain.comment.Comment;
import site.study.post.repository.jpa.JpaCommentRepository;
import site.study.post.repository.jpa.JpaLikeRepository;
import site.study.post.repository.jpa.JpaPostRepository;
import site.study.post.repository.jpa.entity.like.LikeEntity;
import site.study.user.domain.User;

@Repository
@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    private final JpaPostRepository jpaPostRepository;
    private final JpaCommentRepository jpaCommentRepository;
    private final JpaLikeRepository jpaLikeRepository;

    @Override
    public boolean checkLike(Post post, User user) {
        LikeEntity entity = new LikeEntity(post, user);
        return jpaLikeRepository.existsById(entity.getId());
    }

    @Override
    public boolean checkLike(Comment comment, User user) {
        LikeEntity entity = new LikeEntity(comment, user);
        return jpaLikeRepository.existsById(entity.getId());
    }

    @Override
    @Transactional
    public void like(Post post, User user) {
        LikeEntity entity = new LikeEntity(post, user);
        entityManager.persist(entity);
        jpaPostRepository.updateLikeCount(post);
    }

    @Override
    @Transactional
    public void like(Comment comment, User user) {
        LikeEntity entity = new LikeEntity(comment, user);
        entityManager.persist(entity);
        jpaCommentRepository.updateLikeCount(comment);
    }

    @Override
    @Transactional
    public void unlike(Post post, User user) {
        LikeEntity entity = new LikeEntity(post, user);
        jpaLikeRepository.deleteById(entity.getId());
        jpaPostRepository.updateLikeCount(post);
    }

    @Override
    @Transactional
    public void unlike(Comment comment, User user) {
        LikeEntity entity = new LikeEntity(comment, user);
        jpaLikeRepository.deleteById(entity.getId());
        jpaCommentRepository.updateLikeCount(comment);
    }
}
