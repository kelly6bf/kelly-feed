package site.study.post.repository;

import site.study.post.application.port.PostRepository;
import site.study.post.domain.Post;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FakePostRepository implements PostRepository {

    private final Map<Long, Post> store = new HashMap<>();

    @Override
    public Optional<Post> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Post save(Post post) {
        if (post.getId() != null) {
            store.put(post.getId(), post);
            return post;
        }

        long id = store.size() + 1;
        Post newPost = new Post(id, post.getAuthor(), post.getContent());
        store.put(id, newPost);
        return newPost;
    }
}
