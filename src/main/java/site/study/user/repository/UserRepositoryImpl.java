package site.study.user.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import site.study.user.application.port.UserRepository;
import site.study.user.domain.User;
import site.study.user.repository.jpa.JpaUserRepository;
import site.study.user.repository.jpa.entity.UserEntity;

@Repository
@AllArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    @Override
    public User save(User user) {
        UserEntity entity = new UserEntity(user);
        entity = jpaUserRepository.save(entity);
        return entity.toUser();
    }

    @Override
    public User findById(Long id) {
        UserEntity userEntity = jpaUserRepository
            .findById(id)
            .orElseThrow(IllegalArgumentException::new);
        return userEntity.toUser();
    }
}
