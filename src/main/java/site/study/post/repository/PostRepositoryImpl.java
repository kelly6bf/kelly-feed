package site.study.post.repository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import site.study.post.application.port.PostRepository;
import site.study.post.domain.Post;
import site.study.post.repository.jpa.JpaPostRepository;
import site.study.post.repository.jpa.entity.post.PostEntity;
import site.study.post.repository.post_queue.UserPostQueueCommandRepository;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {

    private final JpaPostRepository jpaPostRepository;
    private final UserPostQueueCommandRepository postQueueCommandRepository;

    @Override
    public Post findById(Long id) {
        PostEntity postEntity = jpaPostRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Post not found"));
        return postEntity.toPost();
    }

    @Override
    @Transactional
    public Post save(Post post) {
        PostEntity postEntity = jpaPostRepository.save(new PostEntity(post));
        postQueueCommandRepository.publishPost(postEntity);
        return postEntity.toPost();
    }

    @Override
    public Post publish(Post post) {
        PostEntity postEntity = jpaPostRepository.save(new PostEntity(post));
        postQueueCommandRepository.publishPost(postEntity);
        return postEntity.toPost();
    }
}
