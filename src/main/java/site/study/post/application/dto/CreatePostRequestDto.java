package site.study.post.application.dto;

import site.study.post.domain.PostPublicationState;

public record CreatePostRequestDto(
    Long userId,
    String content,
    PostPublicationState state
) {
}
