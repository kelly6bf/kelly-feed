package site.study.post.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.study.common.ui.Response;
import site.study.post.application.dto.GetPostContentResponseDto;
import site.study.post.repository.post_queue.UserPostQueueQueryRepository;

import java.util.List;

@RestController
@RequestMapping("/api/feed")
@RequiredArgsConstructor
public class FeedController {

    private final UserPostQueueQueryRepository userPostQueueQueryRepository;

    @GetMapping("/{userId}")
    public Response<List<GetPostContentResponseDto>> getPostFeedList(
        @PathVariable(name = "userId") Long userId,
        Long lastContentId
    ) {
        List<GetPostContentResponseDto> contentResponse = userPostQueueQueryRepository.getContentResponse(userId, lastContentId);
        return Response.ok(contentResponse);
    }
}
