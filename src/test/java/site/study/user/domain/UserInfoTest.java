package site.study.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

class UserInfoTest {

    @DisplayName("UserInfo 객체를 생성한다.")
    @Test
    void createUserInfo() {
        // Given
        final String name = "kelly";
        final String profileImageUrl = "https://image.site";

        // When & Then
        assertThatCode(() -> new UserInfo(name,profileImageUrl))
            .doesNotThrowAnyException();
    }
}
