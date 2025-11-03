package site.study.post.application.dto;

import site.study.post.domain.PostPublicationState;

public record UpdatePostRequestDto(
    Long userId,
    String content,
    PostPublicationState state
) {
}
