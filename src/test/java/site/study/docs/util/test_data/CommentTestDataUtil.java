package site.study.docs.util.test_data;

import site.study.post.application.dto.CreateCommentRequestDto;
import site.study.post.application.dto.CreatePostRequestDto;
import site.study.post.application.dto.UpdateCommentRequestDto;
import site.study.post.domain.Post;
import site.study.post.domain.PostPublicationState;
import site.study.post.domain.comment.Comment;
import site.study.post.domain.content.CommentContent;
import site.study.user.domain.User;
import site.study.user.domain.UserInfo;

public class CommentTestDataUtil {

    public static Comment createCommentByCreateCommentRequestDto(final CreateCommentRequestDto dto) {
        final User user = new User(dto.userId(), new UserInfo("kelly", "https://image-kelly.site"));
        final Post post = PostTestDataUtil.createPostByCreatePostRequestDto(new CreatePostRequestDto(
            2L,
            "치킨 머거야지 히히",
            PostPublicationState.PUBLIC
        ));

        return new Comment(
            1L,
            post,
            user,
            new CommentContent(dto.content())
        );
    }

    public static Comment createCommentByUpdateCommentRequestDto(final UpdateCommentRequestDto dto) {
        final User user = new User(dto.userId(), new UserInfo("kelly", "https://image-kelly.site"));
        final Post post = PostTestDataUtil.createPostByCreatePostRequestDto(new CreatePostRequestDto(
            2L,
            "치킨 머거야지 히히",
            PostPublicationState.PUBLIC
        ));

        return new Comment(
            1L,
            post,
            user,
            new CommentContent(dto.content())
        );
    }
}
