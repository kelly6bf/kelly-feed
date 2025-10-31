package site.study.user.application;

import org.springframework.stereotype.Service;
import site.study.user.application.dto.FollowUserRequestDto;
import site.study.user.application.port.UserRelationRepository;
import site.study.user.domain.User;

@Service
public class UserRelationService {

    private final UserService userService;
    private final UserRelationRepository userRelationRepository;

    public UserRelationService(final UserService userService, final UserRelationRepository userRelationRepository) {
        this.userService = userService;
        this.userRelationRepository = userRelationRepository;
    }

    public void follow(final FollowUserRequestDto requestDto) {
        final User user = userService.getUser(requestDto.userId());
        final User targetUser = userService.getUser(requestDto.targetUserId());

        if (userRelationRepository.isAlreadyFollow(user, targetUser)) {
            throw new IllegalArgumentException();
        }

        user.follow(targetUser);
        userRelationRepository.save(user, targetUser);
    }

    public void unfollow(final FollowUserRequestDto requestDto) {
        final User user = userService.getUser(requestDto.userId());
        final User targetUser = userService.getUser(requestDto.targetUserId());

        if (!userRelationRepository.isAlreadyFollow(user, targetUser)) {
            throw new IllegalArgumentException();
        }

        user.unfollow(targetUser);
        userRelationRepository.delete(user, targetUser);
    }
}
