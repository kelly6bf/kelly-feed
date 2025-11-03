package site.study.post.repository.post_queue;

import site.study.post.repository.jpa.entity.post.PostEntity;

public interface UserPostQueueCommandRepository {

    void publishPost(PostEntity postEntity);

    void saveFollowPost(Long userId, Long targetId);

    void deleteUnfollowPost(Long userId, Long targetId);
}
