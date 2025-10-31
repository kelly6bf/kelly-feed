package site.study.post.application;

import site.study.fake.FakeObjectFactory;
import site.study.post.application.dto.CreatePostRequestDto;
import site.study.post.domain.Post;
import site.study.post.domain.PostPublicationState;
import site.study.user.application.UserService;
import site.study.user.application.dto.CreateUserRequestDto;
import site.study.user.domain.User;

public class PostApplicationTestTemplate {

    final UserService userService = FakeObjectFactory.getUserService();
    final PostService postService = FakeObjectFactory.getPostService();

    final User user = userService.createUser(new CreateUserRequestDto("user1", null));;
    final User otherUser = userService.createUser(new CreateUserRequestDto("user1", null));;

    CreatePostRequestDto dto = new CreatePostRequestDto(user.getId(), "this is test content", PostPublicationState.PUBLIC);
    final Post post = postService.createPost(dto);
}
