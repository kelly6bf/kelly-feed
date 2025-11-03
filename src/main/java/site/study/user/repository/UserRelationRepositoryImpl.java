package site.study.user.repository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import site.study.post.repository.post_queue.UserPostQueueCommandRepository;
import site.study.user.application.port.UserRelationRepository;
import site.study.user.domain.User;
import site.study.user.repository.jpa.JpaUserRelationRepository;
import site.study.user.repository.jpa.JpaUserRepository;
import site.study.user.repository.jpa.entity.UserEntity;
import site.study.user.repository.jpa.entity.UserRelationEntity;
import site.study.user.repository.jpa.entity.UserRelationId;

import java.util.List;

@Repository
@AllArgsConstructor
public class UserRelationRepositoryImpl implements UserRelationRepository {

    private final JpaUserRepository jpaUserRepository;
    private final JpaUserRelationRepository jpaUserRelationRepository;
    private final UserPostQueueCommandRepository userPostQueueCommandRepository;

    @Override
    public boolean isAlreadyFollow(User user, User targetUser) {
        UserRelationId id = new UserRelationId(user.getId(), targetUser.getId());
        return jpaUserRelationRepository.existsById(id);
    }

    @Override
    @Transactional
    public void save(User user, User targetUser) {
        UserRelationEntity entity = new UserRelationEntity(user.getId(), targetUser.getId());
        jpaUserRelationRepository.save(entity);
        jpaUserRepository.saveAll(List.of(new UserEntity(user), new UserEntity(targetUser)));
        userPostQueueCommandRepository.saveFollowPost(user.getId(), targetUser.getId());
    }

    @Override
    @Transactional
    public void delete(User user, User targetUser) {
        UserRelationId id = new UserRelationId(user.getId(), targetUser.getId());
        jpaUserRelationRepository.deleteById(id);
        jpaUserRepository.saveAll(List.of(new UserEntity(user), new UserEntity(targetUser)));
        userPostQueueCommandRepository.deleteUnfollowPost(user.getId(), targetUser.getId());
    }
}
