package site.study.user.application.port;

import site.study.user.domain.User;

public interface UserRepository {

    User save(User user);

    User findById(Long id);
}
