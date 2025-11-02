package site.study.post.application.port;

import site.study.post.domain.Post;

public interface PostRepository {

    Post save(Post post);

    Post findById(Long id);

    Post publish(Post post);
}
