package site.study.docs.post;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import site.study.docs.util.PostTestDataUtil;
import site.study.docs.util.RestDocsSupport;
import site.study.post.application.PostService;
import site.study.post.application.dto.CreatePostRequestDto;
import site.study.post.domain.PostPublicationState;
import site.study.post.ui.PostController;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
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
}
