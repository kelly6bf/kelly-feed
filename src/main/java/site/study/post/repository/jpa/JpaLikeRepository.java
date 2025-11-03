package site.study.post.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import site.study.post.repository.jpa.entity.like.LikeEntity;
import site.study.post.repository.jpa.entity.like.LikeId;

public interface JpaLikeRepository extends JpaRepository<LikeEntity, LikeId> {
}
