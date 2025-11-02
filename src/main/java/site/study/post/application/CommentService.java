package site.study.post.application;

import org.springframework.stereotype.Service;
import site.study.post.application.dto.CreateCommentRequestDto;
import site.study.post.application.dto.LikeRequestDto;
import site.study.post.application.dto.UpdateCommentRequestDto;
import site.study.post.application.port.CommentRepository;
import site.study.post.application.port.LikeRepository;
import site.study.post.domain.Post;
import site.study.post.domain.comment.Comment;
import site.study.post.domain.content.CommentContent;
import site.study.user.application.UserService;
import site.study.user.domain.User;

@Service
public class CommentService {

    private final UserService userService;
    private final PostService postService;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    public CommentService(
        final UserService userService,
        final PostService postService,
        final CommentRepository commentRepository,
        final LikeRepository likeRepository
    ) {
        this.userService = userService;
        this.postService = postService;
        this.commentRepository = commentRepository;
        this.likeRepository = likeRepository;
    }

    public Comment getComment(final Long id) {
        return commentRepository.findById(id);
    }

    public Comment createComment(final CreateCommentRequestDto requestDto) {
        final Post post = postService.getPost(requestDto.postId());
        final User author = userService.getUser(requestDto.userId());

        final CommentContent commentContent = new CommentContent(requestDto.content());
        final Comment comment = new Comment(null, post, author, commentContent);
        return commentRepository.save(comment);
    }

    public Comment updateComment(final Long commentId, final UpdateCommentRequestDto requestDto) {
        final Comment comment = getComment(commentId);
        final User author = userService.getUser(requestDto.userId());

        comment.updateComment(author, requestDto.content());
        return commentRepository.save(comment);
    }

    public void likeComment(final LikeRequestDto requestDto) {
        final Comment comment = getComment(requestDto.targetId());
        final User author = userService.getUser(requestDto.userId());

        if (likeRepository.checkLike(comment, author)) {
            return;
        }

        comment.like(author);
        likeRepository.like(comment, author);
    }

    public void unlikeComment(final LikeRequestDto requestDto) {
        final Comment comment = getComment(requestDto.targetId());
        final User author = userService.getUser(requestDto.userId());

        if (!likeRepository.checkLike(comment, author)) {
            return;
        }

        comment.unlike(author);
        likeRepository.unlike(comment, author);
    }
}
