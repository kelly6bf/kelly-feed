package site.study.fake;

import site.study.post.application.CommentService;
import site.study.post.application.PostService;
import site.study.post.application.port.CommentRepository;
import site.study.post.application.port.LikeRepository;
import site.study.post.application.port.PostRepository;
import site.study.post.repository.FakeCommentRepository;
import site.study.post.repository.FakeLikeRepository;
import site.study.post.repository.FakePostRepository;
import site.study.user.application.UserRelationService;
import site.study.user.application.UserService;
import site.study.user.application.port.UserRelationRepository;
import site.study.user.application.port.UserRepository;
import site.study.user.repository.FakeUserRelationRepository;
import site.study.user.repository.FakeUserRepository;

public class FakeObjectFactory {

    private static final UserRepository fakeUserRepository = new FakeUserRepository();
    private static final UserRelationRepository fakeUserRelationRepository = new FakeUserRelationRepository();
    private static final PostRepository fakePostRepository = new FakePostRepository();
    private static final CommentRepository fakeCommentRepository = new FakeCommentRepository();
    private static final LikeRepository fakeLikeRepository = new FakeLikeRepository();

    private static final UserService userService = new UserService(fakeUserRepository);
    private static final UserRelationService userRelationService = new UserRelationService(userService, fakeUserRelationRepository);
    private static final PostService postService = new PostService(userService, fakePostRepository, fakeLikeRepository);
    private static final CommentService commentService = new CommentService(userService, postService, fakeCommentRepository, fakeLikeRepository);

    private FakeObjectFactory() {
    }

    public static UserService getUserService() {
        return userService;
    }

    public static UserRelationService getUserRelationService() {
        return userRelationService;
    }

    public static PostService getPostService() {
        return postService;
    }

    public static CommentService getCommentService() {
        return commentService;
    }
}
