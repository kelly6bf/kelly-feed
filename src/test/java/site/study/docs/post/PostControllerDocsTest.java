package site.study.docs.post;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import site.study.docs.util.test_data.PostTestDataUtil;
import site.study.docs.util.RestDocsSupport;
import site.study.post.application.PostService;
import site.study.post.application.dto.CreatePostRequestDto;
import site.study.post.application.dto.LikeRequestDto;
import site.study.post.application.dto.UpdatePostRequestDto;
import site.study.post.domain.PostPublicationState;
import site.study.post.ui.PostController;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("게시글 API 문서화 테스트")
public class PostControllerDocsTest extends RestDocsSupport {

    private final PostService postService = mock(PostService.class);

    @Override
    protected Object initController() {
        return new PostController(postService);
    }
    
    @DisplayName("게시글 생성 API")
    @Test
    void createPost() throws Exception {
        // Given
        final CreatePostRequestDto request = new CreatePostRequestDto(
            1L,
            "켈리의 게시글",
            PostPublicationState.PUBLIC
        );

        given(postService.createPost(any())).willReturn(PostTestDataUtil.createPostByCreatePostRequestDto(request));

        // When & Then
        mockMvc.perform(
            post("/api/post")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(convertToJson(request)))
            .andExpect(status().isOk())
            .andDo(createDocument(
                requestFields(
                    fieldWithPath("userId")
                        .description("게시글을 작성하는 회원 ID")
                        .type(JsonFieldType.NUMBER)
                        .attributes(constraints("존재하는 회원 ID만 입력 가능")),
                    fieldWithPath("content")
                        .description("게시글 내용")
                        .type(JsonFieldType.STRING)
                        .attributes(constraints("5 ~ 500 길이의 문자열만 입력 가능")),
                    fieldWithPath("state")
                        .description("게시글 공개 상태")
                        .type(JsonFieldType.STRING)
                        .attributes(constraints("존재하는 옵션값만 입력 가능"))
                        .attributes(options("PUBLIC(전체 공개)", "ONLY_FOLLOWED(팔로워들만 공개)", "PRIVATE(비공개)"))),
                responseFields(
                    fieldWithPath("code")
                        .description("API 응답 코드")
                        .type(JsonFieldType.NUMBER),
                    fieldWithPath("message")
                        .description("API 응답 메시지")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("value")
                        .description("작성한 게시글 ID")
                        .type(JsonFieldType.NUMBER))
                ));
    }

    @DisplayName("게시글 수정 API")
    @Test
    void updatePost() throws Exception {
        // Given
        final Long postId = 1L;
        final UpdatePostRequestDto request = new UpdatePostRequestDto(
            1L,
            "이제 팔친님들만 볼 수 있음 ㅋ",
            PostPublicationState.ONLY_FOLLOWED
        );

        given(postService.updatePost(any(), any())).willReturn(PostTestDataUtil.createPostByUpdatePostRequestDto(request));

        // When & Then
        mockMvc.perform(
                patch("/api/post/{postId}", postId)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(convertToJson(request)))
            .andExpect(status().isOk())
            .andDo(createDocument(
                pathParameters(
                    parameterWithName("postId")
                        .description("수정할 게시글 ID")
                        .attributes(constraints("내가 작성한 게시글 ID만 입력 가능"), pathVariableExample(postId))
                ),
                requestFields(
                    fieldWithPath("userId")
                        .description("게시글을 수정하는 회원 ID")
                        .type(JsonFieldType.NUMBER)
                        .attributes(constraints("존재하는 회원 ID만 입력 가능")),
                    fieldWithPath("content")
                        .description("게시글 내용")
                        .type(JsonFieldType.STRING)
                        .attributes(constraints("5 ~ 500 길이의 문자열만 입력 가능")),
                    fieldWithPath("state")
                        .description("게시글 공개 상태")
                        .type(JsonFieldType.STRING)
                        .attributes(constraints("존재하는 옵션값만 입력 가능"))
                        .attributes(options("PUBLIC(전체 공개)", "ONLY_FOLLOWED(팔로워들만 공개)", "PRIVATE(비공개)"))),
                responseFields(
                    fieldWithPath("code")
                        .description("API 응답 코드")
                        .type(JsonFieldType.NUMBER),
                    fieldWithPath("message")
                        .description("API 응답 메시지")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("value")
                        .description("수정한 게시글 ID")
                        .type(JsonFieldType.NUMBER))
            ));
    }

    @DisplayName("게시글 좋아요 생성 API")
    @Test
    void likePost() throws Exception {
        // Given
        final LikeRequestDto request = new LikeRequestDto(3L, 1L);

        // When & Then
        mockMvc.perform(
                post("/api/post/like")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(convertToJson(request)))
            .andExpect(status().isOk())
            .andDo(createDocument(
                requestFields(
                    fieldWithPath("targetId")
                        .description("좋아요를 생성할 게시글 ID")
                        .type(JsonFieldType.NUMBER)
                        .attributes(constraints("좋아요를 누를 수 있는 상태의 게시글 ID만 입력 가능")),
                    fieldWithPath("userId")
                        .description("좋아요를 생성하는 회원 ID")
                        .type(JsonFieldType.NUMBER)
                        .attributes(constraints("게시글 작성자 외 해당 게시글에 좋아요를 누르지 않은 사용자 ID만 입력 가능"))
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

    @DisplayName("게시글 좋아요 삭제 API")
    @Test
    void unlikePost() throws Exception {
        // Given
        final LikeRequestDto request = new LikeRequestDto(3L, 1L);

        // When & Then
        mockMvc.perform(
                post("/api/post/unlike")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(convertToJson(request)))
            .andExpect(status().isOk())
            .andDo(createDocument(
                requestFields(
                    fieldWithPath("targetId")
                        .description("좋아요를 삭제할 게시글 ID")
                        .type(JsonFieldType.NUMBER)
                        .attributes(constraints("좋아요를 삭제 할 수 있는 상태의 게시글 ID만 입력 가능")),
                    fieldWithPath("userId")
                        .description("좋아요를 생성하는 회원 ID")
                        .type(JsonFieldType.NUMBER)
                        .attributes(constraints("삭제할 좋아요를 생성한 사용자 ID만 입력 가능"))
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
