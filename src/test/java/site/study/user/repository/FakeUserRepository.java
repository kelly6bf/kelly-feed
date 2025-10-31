package site.study.user.repository;

import site.study.user.application.port.UserRepository;
import site.study.user.domain.User;

import java.util.HashMap;
import java.util.Map;

public class FakeUserRepository implements UserRepository {

    private final Map<Long, User> store = new HashMap<>();

    @Override
    public User save(final User user) {
        if (user.getId() != null) {
            store.put(user.getId(), user);
        }

        final long id = store.size() + 1L;
        final User newUser = new User(id, user.getUserInfo());
        store.put(id, newUser);
        return newUser;
    }

    @Override
    public User findById(final Long id) {
        return store.get(id);
    }
}
