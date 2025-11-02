package site.study.docs.util.test_data;

import site.study.common.domain.PositiveIntegerCounter;
import site.study.post.application.dto.CreatePostRequestDto;
import site.study.post.application.dto.UpdatePostRequestDto;
import site.study.post.domain.Post;
import site.study.post.domain.content.PostContent;
import site.study.user.domain.User;
import site.study.user.domain.UserInfo;

public class PostTestDataUtil {

    public static Post createPostByCreatePostRequestDto(final CreatePostRequestDto dto) {
        final User user = new User(dto.userId(), new UserInfo("kelly", "https://image-kelly.site"));
        return new Post(
            1L,
            user,
            new PostContent(dto.content()),
            new PositiveIntegerCounter(),
            dto.state()
        );
    }

    public static Post createPostByUpdatePostRequestDto(final UpdatePostRequestDto dto) {
        final User user = new User(dto.userId(), new UserInfo("kelly", "https://image-kelly.site"));
        return new Post(
            1L,
            user,
            new PostContent(dto.content()),
            new PositiveIntegerCounter(),
            dto.state()
        );
    }
}
