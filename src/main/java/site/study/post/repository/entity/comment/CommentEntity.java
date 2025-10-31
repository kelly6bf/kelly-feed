package site.study.post.repository.entity.comment;

import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.study.common.repository.entity.TimeBaseEntity;
import site.study.post.domain.comment.Comment;
import site.study.post.domain.content.CommentContent;
import site.study.post.repository.entity.post.PostEntity;
import site.study.user.repository.entity.UserEntity;

@Entity
@Table(name="community_commnent")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CommentEntity extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="authorId", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private UserEntity author;

    @ManyToOne
    @JoinColumn(name="postId", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private PostEntity post;

    private String content;
    private Integer likeCounter;

    public CommentEntity(Comment comment) {
        this.id = comment.getId();
        this.author = new UserEntity(comment.getAuthor());
        this.post = new PostEntity(comment.getPost());
        this.content = comment.getContentText();
        this.likeCounter = comment.getLikeCount();
    }

    public Comment toComment() {
        return new Comment(
            id,
            post.toPost(),
            author.toUser(),
            new CommentContent(content)
        );
    }
}
