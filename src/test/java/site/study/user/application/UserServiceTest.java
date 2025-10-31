package site.study.user.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import site.study.fake.FakeObjectFactory;
import site.study.user.application.dto.CreateUserRequestDto;
import site.study.user.domain.User;
import site.study.user.domain.UserInfo;

import static org.assertj.core.api.Assertions.assertThat;

class UserServiceTest {

    private final UserService userService = FakeObjectFactory.getUserService();

    @DisplayName("새 사용자를 생성한다.")
    @Test
    void saveUser() {
        // Given
        final CreateUserRequestDto requestDto = new CreateUserRequestDto("kelly", "");

        // When
        final User savedUser = userService.createUser(requestDto);

        // Then
        final User foundUser = userService.getUser(savedUser.getId());
        final UserInfo userInfo = foundUser.getUserInfo();

        assertThat(foundUser.getId()).isEqualTo(savedUser.getId());
        assertThat(userInfo.getName()).isEqualTo(requestDto.name());
    }
}
