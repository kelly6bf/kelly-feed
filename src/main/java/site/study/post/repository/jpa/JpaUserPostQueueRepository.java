package site.study.post.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import site.study.post.repository.jpa.entity.post.UserPostQueueEntity;

public interface JpaUserPostQueueRepository extends JpaRepository<UserPostQueueEntity, Long> {

    void deleteAllByUserIdAndAuthorId(Long userId, Long authorId);
}
