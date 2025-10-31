package site.study.post.domain.comment;

import org.junit.jupiter.api.Test;
import site.study.post.domain.Post;
import site.study.post.domain.PostPublicationState;
import site.study.post.domain.content.CommentContent;
import site.study.post.domain.content.PostContent;
import site.study.user.domain.User;
import site.study.user.domain.UserInfo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommentTest {

    private final User user = new User(1L, new UserInfo("name", "url"));
    private final User otherUser = new User(2L, new UserInfo("name", "url"));

    private final Post post = new Post(1L, user, new PostContent("content"), PostPublicationState.PUBLIC);
    private final Comment comment = new Comment(1L, post, user, new CommentContent("comment content"));

    @Test
    void givenCommentWhenLikeThenLikeCountShouldBe1() {
        // when
        comment.like(otherUser);

        // then
        assertEquals(1, comment.getLikeCount());
    }

    @Test
    void givenCommentWhenLikeBySameUserThenLikeCountShouldThrowError() {
        // when, then
        assertThrows(IllegalArgumentException.class, () -> comment.like(user));
    }

    @Test
    void givenCommentCreatedAndLikeWhenUnlikeThenLikeCountShouldBe0() {
        // given
        comment.getLikeCount();

        // when
        comment.unlike(otherUser);

        // then
        assertEquals(0, comment.getLikeCount());
    }

    @Test
    void givenCommentCreatedWhenUnlikeThenLikeCountShouldBe0() {
        // when
        comment.unlike(otherUser);

        // then
        assertEquals(0, comment.getLikeCount());
    }

    @Test
    void givenCommentWhenUpdateContentThenContentShouldBeUpdated() {
        // given
        String newContent = "new content";

        // when
        comment.updateComment(user, newContent);

        // then
        assertEquals(newContent, comment.getContentText());
    }

    @Test
    void givenCommentWhenUpdateContentOver100ThenThrowError() {
        // given
        String newContent = "a".repeat(101);

        // when, then
        assertThrows(IllegalArgumentException.class, () -> comment.updateComment(user, newContent));
    }
}
