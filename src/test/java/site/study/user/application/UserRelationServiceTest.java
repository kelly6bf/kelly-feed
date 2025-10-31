package site.study.user.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import site.study.fake.FakeObjectFactory;
import site.study.user.application.dto.CreateUserRequestDto;
import site.study.user.application.dto.FollowUserRequestDto;
import site.study.user.domain.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserRelationServiceTest {

    private final UserService userService = FakeObjectFactory.getUserService();
    private final UserRelationService userRelationService = FakeObjectFactory.getUserRelationService();

    private User user1;
    private User user2;
    private FollowUserRequestDto followUserRequestDto;

    @BeforeEach
    void init() {
        final CreateUserRequestDto createUserRequestDto = new CreateUserRequestDto("kelly", "");
        this.user1 = userService.createUser(createUserRequestDto);
        this.user2 = userService.createUser(createUserRequestDto);
        this.followUserRequestDto = new FollowUserRequestDto(user1.getId(), user2.getId());
    }
    
    @DisplayName("정상 팔로우 테스트")
    @Test
    void follow() {
        // When
        userRelationService.follow(followUserRequestDto);
        
        // Then
        assertThat(user1.getFollowingCount()).isEqualTo(1);
        assertThat(user2.getFollowerCount()).isEqualTo(1);
    }
    
    @DisplayName("이미 팔로우한 사용자를 팔로우 하려고 하면 예외가 발생한다.")
    @Test
    void followToAlreadyFollower() {
        // Given
        userRelationService.follow(followUserRequestDto);
        
        // When & Then
        assertThatThrownBy(() -> userRelationService.follow(followUserRequestDto))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("자기 자신을 팔로우 하려고 하면 예외가 발생한다.")
    @Test
    void followToMe() {
        // Given
        final FollowUserRequestDto followUserRequestDto = new FollowUserRequestDto(user1.getId(), user1.getId());

        // When & Then
        assertThatThrownBy(() -> userRelationService.follow(followUserRequestDto))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
