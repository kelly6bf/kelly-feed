package site.study.docs.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import site.study.docs.util.RestDocsSupport;
import site.study.user.application.UserRelationService;
import site.study.user.application.dto.FollowUserRequestDto;
import site.study.user.ui.UserRelationController;

import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("회원 릴레이션 API 문서화 테스트")
public class UserRelationControllerDocsTest extends RestDocsSupport {
    
    private final UserRelationService userRelationService = mock(UserRelationService.class);
    
    @Override
    protected Object initController() {
        return new UserRelationController(userRelationService);
    }
    
    @DisplayName("회원 팔로우 API")
    @Test
    void followUser() throws Exception {
        // Given
        final FollowUserRequestDto request = new FollowUserRequestDto(1L, 2L);

        // When & Then
        mockMvc.perform(
                post("/api/relation/follow")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(convertToJson(request)))
            .andExpect(status().isOk())
            .andDo(createDocument(
                requestFields(
                    fieldWithPath("userId")
                        .description("팔로우를 거는 회원 ID")
                        .type(JsonFieldType.NUMBER)
                        .attributes(constraints("존재하는 회원 ID만 입력 가능")),
                    fieldWithPath("targetUserId")
                        .description("팔로우 대상 회원 ID")
                        .type(JsonFieldType.NUMBER)
                        .attributes(constraints("본인 ID 입력 불가, 아직 팔로우 하고 있지 않은 회원 ID만 입력 가능"))
                ),
                responseFields(
                    fieldWithPath("code")
                        .description("API 응답 코드")
                        .type(JsonFieldType.NUMBER),
                    fieldWithPath("message")
                        .description("API 응답 메시지")
                        .type(JsonFieldType.STRING)
            )));
    }

    @DisplayName("회원 언팔로우 API")
    @Test
    void unfollowUser() throws Exception {
        // Given
        final FollowUserRequestDto request = new FollowUserRequestDto(1L, 2L);

        // When & Then
        mockMvc.perform(
                post("/api/relation/unfollow")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(convertToJson(request)))
            .andExpect(status().isOk())
            .andDo(createDocument(
                requestFields(
                    fieldWithPath("userId")
                        .description("언팔로우를 거는 회원 ID")
                        .type(JsonFieldType.NUMBER)
                        .attributes(constraints("존재하는 회원 ID만 입력 가능")),
                    fieldWithPath("targetUserId")
                        .description("언팔로우 대상 회원 ID")
                        .type(JsonFieldType.NUMBER)
                        .attributes(constraints("본인 ID 입력 불가, 팔로우 하고 있는 회원 ID만 입력 가능"))
                ),
                responseFields(
                    fieldWithPath("code")
                        .description("API 응답 코드")
                        .type(JsonFieldType.NUMBER),
                    fieldWithPath("message")
                        .description("API 응답 메시지")
                        .type(JsonFieldType.STRING)
                )));
    }
}
