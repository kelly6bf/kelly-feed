package site.study.docs.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import site.study.docs.util.RestDocsSupport;
import site.study.docs.util.test_data.UserTestDataUtil;
import site.study.user.application.UserService;
import site.study.user.application.dto.CreateUserRequestDto;
import site.study.user.repository.jpa.JpaUserListQueryRepository;
import site.study.user.ui.UserController;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("회원 API 문서화 테스트")
public class UserControllerDocsTest extends RestDocsSupport {

    private final UserService userService = mock(UserService.class);
    private final JpaUserListQueryRepository jpaUserListQueryRepository = mock(JpaUserListQueryRepository.class);

    @Override
    protected Object initController() {
        return new UserController(userService, jpaUserListQueryRepository);
    }

    @DisplayName("회원 생성 API")
    @Test
    void createUser() throws Exception {
        // Given
        final CreateUserRequestDto request = new CreateUserRequestDto("kelly", "https://image.site");

        given(userService.createUser(any())).willReturn(UserTestDataUtil.createUserByCreateUserRequestDto(request));

        // When & Then
        mockMvc.perform(
                post("/api/user")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(convertToJson(request)))
            .andExpect(status().isOk())
            .andDo(createDocument(
                requestFields(
                    fieldWithPath("name")
                        .description("회원 이름")
                        .type(JsonFieldType.STRING)
                        .attributes(constraints("공백이 아닌 문자열만 입력 가능")),
                    fieldWithPath("profileImageUrl")
                        .description("회원 프로필 이미지 URL")
                        .type(JsonFieldType.STRING)
                        .optional()
                        .attributes(constraints("유효한 URL만 입력 가능"))
                ),
                responseFields(
                    fieldWithPath("code")
                        .description("API 응답 코드")
                        .type(JsonFieldType.NUMBER),
                    fieldWithPath("message")
                        .description("API 응답 메시지")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("value")
                        .description("생성된 회원 ID")
                        .type(JsonFieldType.NUMBER))
            ));
    }

    @DisplayName("회원 프로필 조회 API")
    @Test
    void getUserResponse() throws Exception {
        // Given
        final Long userId = 1L;

        given(userService.getUserProfile(any())).willReturn(UserTestDataUtil.createGetUserResponseDtoByUserId(userId));

        // When & Then
        mockMvc.perform(
            get("/api/user/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andDo(createDocument(
                pathParameters(
                    parameterWithName("userId")
                        .description("프로필을 조회할 회원 ID")
                        .attributes(constraints("존재하는 회원 ID만 입력 가능"), pathVariableExample(userId))
                ),
                responseFields(
                    fieldWithPath("code")
                        .description("API 응답 코드")
                        .type(JsonFieldType.NUMBER),
                    fieldWithPath("message")
                        .description("API 응답 메시지")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("value")
                        .description("회원 프로필 정보")
                        .type(JsonFieldType.OBJECT),
                    fieldWithPath("value.id")
                        .description("회원 ID")
                        .type(JsonFieldType.NUMBER),
                    fieldWithPath("value.name")
                        .description("회원 이름")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("value.profileImage")
                        .description("회원 프로필 이미지 URL")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("value.followingCount")
                        .description("회원 팔로잉 수")
                        .type(JsonFieldType.NUMBER),
                    fieldWithPath("value.followerCount")
                        .description("회원 팔로워 수")
                        .type(JsonFieldType.NUMBER)
                )));
    }

    @DisplayName("특정 회원의 팔로워 리스트 전체 조회 API")
    @Test
    void getFollowerList() throws Exception {
        // Given
        final Long userId = 1L;

        given(jpaUserListQueryRepository.getFollowerList(any())).willReturn(UserTestDataUtil.createGetUserListResponseDtoList());

        // When & Then
        mockMvc.perform(
                get("/api/user/{userId}/follower", userId)
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andDo(createDocument(
                pathParameters(
                    parameterWithName("userId")
                        .description("팔로워 리스트를 조회할 회원 ID")
                        .attributes(constraints("존재하는 회원 ID만 입력 가능"), pathVariableExample(userId))
                ),
                responseFields(
                    fieldWithPath("code")
                        .description("API 응답 코드")
                        .type(JsonFieldType.NUMBER),
                    fieldWithPath("message")
                        .description("API 응답 메시지")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("value")
                        .description("팔로워 리스트")
                        .type(JsonFieldType.ARRAY),
                    fieldWithPath("value[].name")
                        .description("팔로워 이름")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("value[].profileImage")
                        .description("팔로워 프로필 이미지 URL")
                        .type(JsonFieldType.STRING)
                )));
    }

    @DisplayName("특정 회원의 팔로잉 리스트 전체 조회 API")
    @Test
    void getFollowingList() throws Exception {
        // Given
        final Long userId = 1L;

        given(jpaUserListQueryRepository.getFollowingList(any())).willReturn(UserTestDataUtil.createGetUserListResponseDtoList());

        // When & Then
        mockMvc.perform(
                get("/api/user/{userId}/following", userId)
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andDo(createDocument(
                pathParameters(
                    parameterWithName("userId")
                        .description("팔로잉 리스트를 조회할 회원 ID")
                        .attributes(constraints("존재하는 회원 ID만 입력 가능"), pathVariableExample(userId))
                ),
                responseFields(
                    fieldWithPath("code")
                        .description("API 응답 코드")
                        .type(JsonFieldType.NUMBER),
                    fieldWithPath("message")
                        .description("API 응답 메시지")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("value")
                        .description("팔로잉 리스트")
                        .type(JsonFieldType.ARRAY),
                    fieldWithPath("value[].name")
                        .description("팔로우 하는 회원 이름")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("value[].profileImage")
                        .description("팔로우 하는 회원 프로필 이미지 URL")
                        .type(JsonFieldType.STRING)
                )));
    }
}
