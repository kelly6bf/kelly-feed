package site.study.docs.post;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import site.study.docs.util.RestDocsSupport;
import site.study.docs.util.test_data.FeedTestDataUtil;
import site.study.post.repository.post_queue.UserPostQueueQueryRepository;
import site.study.post.repository.querydsl.QueryDslCommentRepository;
import site.study.post.ui.FeedController;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("게시글 피드 API 문서화 테스트")
public class FeedControllerDocsTest extends RestDocsSupport {

    private final UserPostQueueQueryRepository userPostQueueQueryRepository = mock(UserPostQueueQueryRepository.class);
    private final QueryDslCommentRepository queryDslCommentRepository = mock(QueryDslCommentRepository.class);

    @Override
    protected Object initController() {
        return new FeedController(userPostQueueQueryRepository, queryDslCommentRepository);
    }

    @DisplayName("내가 팔로우 중인 회원의 피드 전체 조회 API")
    @Test
    void getFeedList() throws Exception {
        // Given
        final Long userId = 1L;
        final Long lastContentId = 10L;

        given(userPostQueueQueryRepository.getContentResponse(any(), any())).willReturn(FeedTestDataUtil.createFeedResponseData());

        // When & Then
        mockMvc.perform(
                get("/api/feed/{userId}", userId)
                    .param("lastContentId", String.valueOf(lastContentId))
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
            .andExpect(status().isOk())
            .andDo(createDocument(
                pathParameters(
                    parameterWithName("userId")
                        .description("피드를 조회하는 회원 ID")
                        .attributes(constraints("존재하는 회원 ID만 입력 가능"), pathVariableExample(userId))
                ),
                queryParameters(
                    parameterWithName("lastContentId")
                        .description("마지막으로 조회한 피드 ID")
                        .optional()
                        .attributes(constraints("조회 가능한 피드 ID만 입력 가능"))
                ),
                responseFields(
                    fieldWithPath("code")
                        .description("API 응답 코드")
                        .type(JsonFieldType.NUMBER),
                    fieldWithPath("message")
                        .description("API 응답 메시지")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("value")
                        .description("조회한 피드 리스트")
                        .type(JsonFieldType.ARRAY),
                    fieldWithPath("value[].id")
                        .description("게시글 피드 ID")
                        .type(JsonFieldType.NUMBER),
                    fieldWithPath("value[].content")
                        .description("게시글 피드 내용")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("value[].userId")
                        .description("게시글 피드 작성자 ID")
                        .type(JsonFieldType.NUMBER),
                    fieldWithPath("value[].userName")
                        .description("게시글 피드 작성자 이름")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("value[].userProfileImage")
                        .description("게시글 피드 작성자 프로필 이미지 URL")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("value[].createdAt")
                        .description("게시글 피드 생성일")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("value[].updatedAt")
                        .description("게시글 피드 수정일")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("value[].likeCount")
                        .description("게시글 피드 좋아요 수")
                        .type(JsonFieldType.NUMBER),
                    fieldWithPath("value[].commentCount")
                        .description("게시글 피드 댓글 수")
                        .type(JsonFieldType.NUMBER),
                    fieldWithPath("value[].likedByMe")
                        .description("내가 좋아요를 눌렀는지 여부")
                        .type(JsonFieldType.BOOLEAN)
                )));
    }

    @DisplayName("특정 피드의 댓글 리스트 조회 API")
    @Test
    void getFeedCommentList() throws Exception {
        // Given
        final Long feedId = 3L;
        final Long userId = 1L;
        final Long lastContentId = 10L;

        given(queryDslCommentRepository.getCommentContents(any(), any(), any())).willReturn(FeedTestDataUtil.createFeedCommentResponseData());

        // When & Then
        mockMvc.perform(
                get("/api/feed/{feedId}/comments", feedId)
                    .param("userId", String.valueOf(userId))
                    .param("lastContentId", String.valueOf(lastContentId))
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
            )
            .andExpect(status().isOk())
            .andDo(createDocument(
                pathParameters(
                    parameterWithName("feedId")
                        .description("댓글을 조회할 피드 ID")
                        .attributes(constraints("존재하는 피드 ID만 입력 가능"), pathVariableExample(userId))
                ),
                queryParameters(
                    parameterWithName("userId")
                        .description("댓글을 조회하는 회원 ID")
                        .attributes(constraints("존재하는 회원 ID만 입력 가능")),
                    parameterWithName("lastContentId")
                        .description("마지막으로 조회한 댓글 ID")
                        .optional()
                        .attributes(constraints("조회 가능한 피드 ID만 입력 가능"))
                ),
                responseFields(
                    fieldWithPath("code")
                        .description("API 응답 코드")
                        .type(JsonFieldType.NUMBER),
                    fieldWithPath("message")
                        .description("API 응답 메시지")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("value")
                        .description("조회한 피드 댓글 리스트")
                        .type(JsonFieldType.ARRAY),
                    fieldWithPath("value[].id")
                        .description("게시글 피드 댓 ID")
                        .type(JsonFieldType.NUMBER),
                    fieldWithPath("value[].content")
                        .description("게시글 피드 댓글 내용")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("value[].userId")
                        .description("게시글 피드 댓글 작성자 ID")
                        .type(JsonFieldType.NUMBER),
                    fieldWithPath("value[].userName")
                        .description("게시글 피드 댓글 작성자 이름")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("value[].userProfileImage")
                        .description("게시글 피드 댓글 작성자 프로필 이미지 URL")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("value[].createdAt")
                        .description("게시글 피드 댓글 생성일")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("value[].updatedAt")
                        .description("게시글 피드 댓글 수정일")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("value[].likeCount")
                        .description("게시글 피드 댓글 좋아요 수")
                        .type(JsonFieldType.NUMBER),
                    fieldWithPath("value[].likedByMe")
                        .description("내가 좋아요를 눌렀는지 여부")
                        .type(JsonFieldType.BOOLEAN)
                )));
    }
}
