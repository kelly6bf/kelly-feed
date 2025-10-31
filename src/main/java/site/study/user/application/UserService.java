package site.study.user.application;

import site.study.user.application.dto.CreateUserRequestDto;
import site.study.user.application.port.UserRepository;
import site.study.user.domain.User;
import site.study.user.domain.UserInfo;

public class UserService {

    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(final CreateUserRequestDto requestDto) {
        final UserInfo userInfo = new UserInfo(requestDto.name(), requestDto.profileImageUrl());
        final User user = new User(null, userInfo);
        return userRepository.save(user);
    }

    public User getUser(final Long userId) {
        return userRepository.findById(userId);
    }
}
