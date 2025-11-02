package site.study.post.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.study.common.ui.Response;
import site.study.post.application.PostService;
import site.study.post.application.dto.CreatePostRequestDto;
import site.study.post.application.dto.LikeRequestDto;
import site.study.post.application.dto.UpdatePostRequestDto;
import site.study.post.domain.Post;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public Response<Long> createPost(@RequestBody CreatePostRequestDto dto) {
        Post post = postService.createPost(dto);
        return Response.ok(post.getId());
    }

    @PatchMapping("/{postId}")
    public Response<Long> updatePost(
        @PathVariable(name = "postId") Long postId,
        @RequestBody UpdatePostRequestDto dto
    ) {
        Post post = postService.updatePost(postId, dto);
        return Response.ok(post.getId());
    }

    @PostMapping("/like")
    public Response<Void> likePost(@RequestBody LikeRequestDto dto) {
        postService.likePost(dto);
        return Response.ok(null);
    }

    @PostMapping("/unlike")
    public Response<Void> unlikePost(@RequestBody LikeRequestDto dto) {
        postService.unlikePost(dto);
        return Response.ok(null);
    }
}
