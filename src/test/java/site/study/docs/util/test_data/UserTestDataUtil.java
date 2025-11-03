package site.study.docs.util.test_data;

import site.study.common.domain.PositiveIntegerCounter;
import site.study.user.application.dto.CreateUserRequestDto;
import site.study.user.application.dto.GetUserListResponseDto;
import site.study.user.application.dto.GetUserResponseDto;
import site.study.user.domain.User;
import site.study.user.domain.UserInfo;

import java.util.List;

public class UserTestDataUtil {

    public static User createUserByCreateUserRequestDto(final CreateUserRequestDto dto) {
        return new User(
            1L,
            new UserInfo(dto.name(), dto.profileImageUrl()),
            new PositiveIntegerCounter(),
            new PositiveIntegerCounter()
        );
    }

    public static GetUserResponseDto createGetUserResponseDtoByUserId(final Long userId) {
        return new GetUserResponseDto(
            userId,
            "kelly",
            "https://image.site",
            7,
            182
        );
    }

    public static List<GetUserListResponseDto> createGetUserListResponseDtoList() {
        return List.of(
            new GetUserListResponseDto("elmo", "https://image.site"),
            new GetUserListResponseDto("cola", "https://image.site"),
            new GetUserListResponseDto("mega", "https://image.site"),
            new GetUserListResponseDto("compose", "https://image.site"),
            new GetUserListResponseDto("kermit", "https://image.site")
        );
    }
}
