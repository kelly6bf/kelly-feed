package site.study.post.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.study.common.ui.Response;
import site.study.post.application.dto.GetCommentContentResponseDto;
import site.study.post.application.dto.GetPostContentResponseDto;
import site.study.post.repository.post_queue.UserPostQueueQueryRepository;
import site.study.post.repository.querydsl.QueryDslCommentRepository;

import java.util.List;

@RestController
@RequestMapping("/api/feed")
@RequiredArgsConstructor
public class FeedController {

    private final UserPostQueueQueryRepository userPostQueueQueryRepository;
    private final QueryDslCommentRepository queryDslCommentRepository;

    @GetMapping("/{userId}")
    public Response<List<GetPostContentResponseDto>> getPostFeedList(
        @PathVariable(name = "userId") Long userId,
        @RequestParam(required = false) Long lastContentId
    ) {
        List<GetPostContentResponseDto> contentResponse = userPostQueueQueryRepository.getContentResponse(userId, lastContentId);
        return Response.ok(contentResponse);
    }

    @GetMapping("/{feedId}/comments")
    public Response<List<GetCommentContentResponseDto>> getFeedCommentList(
        @PathVariable(name = "feedId") Long feedId,
        @RequestParam Long userId,
        @RequestParam(required = false) Long lastContentId
    ) {
        final List<GetCommentContentResponseDto> commentContents = queryDslCommentRepository.getCommentContents(feedId, userId, lastContentId);
        return Response.ok(commentContents);
    }
}
