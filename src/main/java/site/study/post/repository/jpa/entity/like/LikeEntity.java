package site.study.post.repository.jpa.entity.like;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.study.common.repository.jpa.entity.TimeBaseEntity;
import site.study.post.domain.Post;
import site.study.post.domain.comment.Comment;
import site.study.user.domain.User;

@Entity
@Table(name="community_like")
@NoArgsConstructor
@Getter
public class LikeEntity extends TimeBaseEntity {

    @EmbeddedId
    private LikeId id;

    public LikeEntity(Post post, User likeedUser) {
        this.id = new LikeId(post.getId(), likeedUser.getId(), LikeTarget.POST.name());
    }

    public LikeEntity(Comment comment, User likeedUser) {
        this.id = new LikeId(comment.getId(), likeedUser.getId(), LikeTarget.COMMENT.name());
    }
}
