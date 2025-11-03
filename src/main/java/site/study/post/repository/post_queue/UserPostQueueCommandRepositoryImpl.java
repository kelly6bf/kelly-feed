package site.study.post.repository.post_queue;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import site.study.post.repository.jpa.JpaPostRepository;
import site.study.post.repository.jpa.JpaUserPostQueueRepository;
import site.study.post.repository.jpa.entity.post.PostEntity;
import site.study.post.repository.jpa.entity.post.UserPostQueueEntity;
import site.study.user.repository.jpa.JpaUserRelationRepository;
import site.study.user.repository.jpa.entity.UserEntity;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserPostQueueCommandRepositoryImpl implements UserPostQueueCommandRepository {

    private final JpaPostRepository jpaPostRepository;
    private final JpaUserRelationRepository jpaUserRelationRepository;
    private final JpaUserPostQueueRepository jpaUserPostQueueRepository;

    @Override
    @Transactional
    public void publishPost(PostEntity postEntity) {
        UserEntity authorEntity = postEntity.getAuthor();
        List<Long> followers = jpaUserRelationRepository.findFollowers(authorEntity.getId());

        List<UserPostQueueEntity> userPostQueueEntities = followers.stream()
            .map(userId -> new UserPostQueueEntity(postEntity.getId(), userId, authorEntity.getId()))
            .toList();

        jpaUserPostQueueRepository.saveAll(userPostQueueEntities);
    }

    @Override
    @Transactional
    public void saveFollowPost(Long userId, Long targetId) {
        List<Long> postIds = jpaPostRepository.findAllPostIdsByAuthorId(targetId);

        List<UserPostQueueEntity> userPostQueueEntities = postIds.stream()
            .map(postId -> new UserPostQueueEntity(postId, userId, targetId))
            .toList();

        jpaUserPostQueueRepository.saveAll(userPostQueueEntities);
    }

    @Override
    @Transactional
    public void deleteUnfollowPost(Long userId, Long targetId) {
        jpaUserPostQueueRepository.deleteAllByUserIdAndAuthorId(userId, targetId);
    }
}
