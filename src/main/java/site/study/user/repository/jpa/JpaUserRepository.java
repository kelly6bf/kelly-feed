package site.study.user.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import site.study.user.repository.jpa.entity.UserEntity;

public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {
}
