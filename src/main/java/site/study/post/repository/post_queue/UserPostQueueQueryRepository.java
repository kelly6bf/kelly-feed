package site.study.post.repository.post_queue;

import site.study.post.application.dto.GetPostContentResponseDto;

import java.util.List;

public interface UserPostQueueQueryRepository {

    List<GetPostContentResponseDto> getContentResponse(Long userId, Long lastPostId);
}
