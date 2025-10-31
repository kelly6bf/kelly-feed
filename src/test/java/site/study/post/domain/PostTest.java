package site.study.post.domain;

import org.junit.jupiter.api.Test;
import site.study.post.domain.content.Content;
import site.study.post.domain.content.PostContent;
import site.study.user.domain.User;
import site.study.user.domain.UserInfo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PostTest {

    private final User user = new User(1L, new UserInfo("name", "url"));
    private final User otherUser = new User(2L, new UserInfo("name", "url"));
    private final Post post = new Post(1L, user, new PostContent("content"), PostPublicationState.PUBLIC);

    @Test
    void givenPostCreatedWhenLikeThenLikeCountShouldBe1() {
        // when
        post.like(otherUser);

        // then
        assertEquals(1, post.getLikeCount());
    }

    @Test
    void givenPostCreatedWhenLikeByOtherUserThenThrowException() {
        // when, then
        assertThrows(IllegalArgumentException.class, () -> post.like(user));
    }

    @Test
    void givenPostCreatedAndLikeWhenUnlikeThenLikeCountShouldBe0() {
        // given
        post.like(otherUser);

        // when
        post.unlike(otherUser);

        // then
        assertEquals(0, post.getLikeCount());
    }

    @Test
    void givenPostCreatedWhenUnlikeThenLikeCountShouldBe0() {
        // when
        post.unlike(otherUser);

        // then
        assertEquals(0, post.getLikeCount());
    }


    @Test
    void givenPostCreatedWhenUpdateContentThenContentShouldBeUpdated() {
        // given
        String newPostContent = "new content";

        // when
        post.updatePost(user, newPostContent, post.getState());

        // then
        Content content = post.getContent();
        assertEquals(newPostContent, content.getContent());
    }

    @Test
    void givenPostCreatedWhenUpdateContentByOtherUserThenThrowException() {
        // given
        String newPostContent = "new content";

        // when, then
        assertThrows(IllegalArgumentException.class, () -> post.updatePost(otherUser, newPostContent, post.getState()));
    }

    @Test
    void givenPostCreatedWhenUpdateStateThenStateShouldBeUpdated() {
        // given
        PostPublicationState newPostState = PostPublicationState.PRIVATE;

        // when
        post.updatePost(post.getAuthor(), post.getContentText(), newPostState);

        // then
        assertEquals(newPostState, post.getState());
    }
}
