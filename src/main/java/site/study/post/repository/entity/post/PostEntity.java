package site.study.post.repository.entity.post;

import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Convert;
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
import org.hibernate.annotations.ColumnDefault;
import site.study.common.domain.PositiveIntegerCounter;
import site.study.common.repository.entity.TimeBaseEntity;
import site.study.post.domain.Post;
import site.study.post.domain.PostPublicationState;
import site.study.post.domain.content.PostContent;
import site.study.user.repository.entity.UserEntity;

@Entity
@Table(name="community_post")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PostEntity extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="author_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private UserEntity author;

    private String content;

    @Convert(converter = PostPublicationStateConverter.class)
    private PostPublicationState publicationState;
    private Integer likeCounter;

    @ColumnDefault("0")
    private int commentCounter;

    public PostEntity(Post post) {
        this.id = post.getId();
        this.author = new UserEntity(post.getAuthor());
        this.content = post.getContentText();
        this.publicationState = post.getState();
        this.likeCounter = post.getLikeCount();
    }

    public Post toPost() {
        return new Post(
            id,
            author.toUser(),
            new PostContent(content),
            new PositiveIntegerCounter(likeCounter),
            publicationState
        );
    }
}
