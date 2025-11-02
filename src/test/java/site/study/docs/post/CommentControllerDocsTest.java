package site.study.docs.post;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import site.study.docs.util.RestDocsSupport;
import site.study.docs.util.test_data.CommentTestDataUtil;
import site.study.post.application.CommentService;
import site.study.post.application.dto.CreateCommentRequestDto;
import site.study.post.application.dto.LikeRequestDto;
import site.study.post.application.dto.UpdateCommentRequestDto;
import site.study.post.ui.CommentController;

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

@DisplayName("게시글 댓글 API 문서화 테스트")
public class CommentControllerDocsTest extends RestDocsSupport {

    private final CommentService commentService = mock(CommentService.class);

    @Override
    protected Object initController() {
        return new CommentController(commentService);
    }

    @DisplayName("게시글 댓글 생성 API")
    @Test
    void createComment() throws Exception {
        // Given
        final CreateCommentRequestDto request = new CreateCommentRequestDto(
            3L,
            1L,
            "저도 먹고 시퍼요..."
        );

        given(commentService.createComment(any())).willReturn(CommentTestDataUtil.createCommentByCreateCommentRequestDto(request));

        // When & Then
        mockMvc.perform(
                post("/api/comment")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(convertToJson(request)))
            .andExpect(status().isOk())
            .andDo(createDocument(
                requestFields(
                    fieldWithPath("postId")
                        .description("댓글을 작성할 게시글 ID")
                        .type(JsonFieldType.NUMBER)
                        .attributes(constraints("존재하는 게시글 ID만 입력 가능")),
                    fieldWithPath("userId")
                        .description("댓글을 작성하는 회원 ID")
                        .type(JsonFieldType.NUMBER)
                        .attributes(constraints("존재하는 회원 ID만 입력 가능")),
                    fieldWithPath("content")
                        .description("댓글 내용")
                        .type(JsonFieldType.STRING)
                        .attributes(constraints("1 ~ 100 길이의 문자열만 입력 가능"))
                ),
                responseFields(
                    fieldWithPath("code")
                        .description("API 응답 코드")
                        .type(JsonFieldType.NUMBER),
                    fieldWithPath("message")
                        .description("API 응답 메시지")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("value")
                        .description("작성한 댓글 ID")
                        .type(JsonFieldType.NUMBER))
            ));
    }

    @DisplayName("게시글 댓글 수정 API")
    @Test
    void updateComment() throws Exception {
        // Given
        final Long commentId = 1L;
        final UpdateCommentRequestDto request = new UpdateCommentRequestDto(
            1L,
            "사실 안먹고 시픔 ㅋ"
        );

        given(commentService.updateComment(any(), any())).willReturn(CommentTestDataUtil.createCommentByUpdateCommentRequestDto(request));

        // When & Then
        mockMvc.perform(
                patch("/api/comment/{commentId}", commentId)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(convertToJson(request)))
            .andExpect(status().isOk())
            .andDo(createDocument(
                pathParameters(
                    parameterWithName("commentId")
                        .description("수정할 댓글 ID")
                        .attributes(constraints("내가 작성한 댓글 ID만 입력 가능"), pathVariableExample(commentId))
                ),
                requestFields(
                    fieldWithPath("userId")
                        .description("댓글을 수정하는 회원 ID")
                        .type(JsonFieldType.NUMBER)
                        .attributes(constraints("존재하는 회원 ID만 입력 가능")),
                    fieldWithPath("content")
                        .description("댓글 내용")
                        .type(JsonFieldType.STRING)
                        .attributes(constraints("1 ~ 100 길이의 문자열만 입력 가능"))),
                responseFields(
                    fieldWithPath("code")
                        .description("API 응답 코드")
                        .type(JsonFieldType.NUMBER),
                    fieldWithPath("message")
                        .description("API 응답 메시지")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("value")
                        .description("수정한 댓글 ID")
                        .type(JsonFieldType.NUMBER))
            ));
    }

    @DisplayName("댓글 좋아요 생성 API")
    @Test
    void likeComment() throws Exception {
        // Given
        final LikeRequestDto request = new LikeRequestDto(1L, 1L);

        // When & Then
        mockMvc.perform(
                post("/api/comment/like")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(convertToJson(request)))
            .andExpect(status().isOk())
            .andDo(createDocument(
                requestFields(
                    fieldWithPath("targetId")
                        .description("좋아요를 생성할 댓글 ID")
                        .type(JsonFieldType.NUMBER)
                        .attributes(constraints("좋아요를 누를 수 있는 상태의 댓글 ID만 입력 가능")),
                    fieldWithPath("userId")
                        .description("좋아요를 생성하는 회원 ID")
                        .type(JsonFieldType.NUMBER)
                        .attributes(constraints("댓글 작성자 외 해당 댓글에 좋아요를 누르지 않은 사용자 ID만 입력 가능"))
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

    @DisplayName("댓글 좋아요 삭제 API")
    @Test
    void unlikeComment() throws Exception {
        // Given
        final LikeRequestDto request = new LikeRequestDto(1L, 1L);

        // When & Then
        mockMvc.perform(
                post("/api/comment/unlike")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(convertToJson(request)))
            .andExpect(status().isOk())
            .andDo(createDocument(
                requestFields(
                    fieldWithPath("targetId")
                        .description("좋아요를 삭제할 댓글 ID")
                        .type(JsonFieldType.NUMBER)
                        .attributes(constraints("좋아요를 삭제 할 수 있는 상태의 댓글 ID만 입력 가능")),
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
