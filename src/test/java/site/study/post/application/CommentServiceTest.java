package site.study.post.application;

import org.junit.jupiter.api.Test;
import site.study.fake.FakeObjectFactory;
import site.study.post.application.dto.CreateCommentRequestDto;
import site.study.post.application.dto.LikeRequestDto;
import site.study.post.application.dto.UpdateCommentRequestDto;
import site.study.post.domain.comment.Comment;
import site.study.post.domain.content.Content;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommentServiceTest extends PostApplicationTestTemplate {

    private final CommentService commentService = FakeObjectFactory.getCommentService();
    private final String commentContent = "this is test comment";

    CreateCommentRequestDto dto = new CreateCommentRequestDto(post.getId(), user.getId(), commentContent);

    @Test
    void givenCreateCommentRequestDtoWhenCreateCommentThenReturnComment() {
        // when
        Comment comment = commentService.createComment(dto);

        // then
        Content content = comment.getContent();
        assertEquals(commentContent, content.getContent());
    }

    @Test
    void givenCreateCommentWhenUpdateCommentThenReturnUpdatedComment() {
        // given
        Comment comment = commentService.createComment(dto);

        // when
        String updatedCommentContent = "this is updated comment";
        UpdateCommentRequestDto updateCommentRequestDto = new UpdateCommentRequestDto(comment.getId(),
            user.getId(), updatedCommentContent);
        Comment updatedComment = commentService.updateComment(updateCommentRequestDto);

        // then
        Content content = updatedComment.getContent();
        assertEquals(updatedCommentContent, content.getContent());
    }

    @Test
    void givenCommentWhenLikeCommentThenReturnCommentWithLike() {
        // given
        Comment comment = commentService.createComment(dto);

        // when
        LikeRequestDto likeRequestDto = new LikeRequestDto(comment.getId(), otherUser.getId());
        commentService.likeComment(likeRequestDto);

        // then
        assertEquals(1, comment.getLikeCount());
    }

    @Test
    void givenCommentWhenUnlikeCommentThenReturnCommentWithoutLike() {
        // given
        Comment comment = commentService.createComment(dto);

        // when
        LikeRequestDto likeRequestDto = new LikeRequestDto(comment.getId(), otherUser.getId());
        commentService.likeComment(likeRequestDto);
        commentService.unlikeComment(likeRequestDto);

        // then
        assertEquals(0, comment.getLikeCount());
    }

    @Test
    void givenCommentWhenLikeSelfThenThrowException() {
        // given
        Comment comment = commentService.createComment(dto);

        // when, then
        LikeRequestDto likeRequestDto = new LikeRequestDto(comment.getId(), user.getId());
        assertThrows(IllegalArgumentException.class, () -> commentService.likeComment(likeRequestDto));
    }
}
