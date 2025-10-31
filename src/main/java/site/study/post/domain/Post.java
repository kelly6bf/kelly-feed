package site.study.post.domain;

import site.study.common.domain.PositiveIntegerCounter;
import site.study.post.domain.content.PostContent;
import site.study.user.domain.User;

public class Post {

    private final Long id;
    private final User author;
    private final PostContent content;
    private final PositiveIntegerCounter likeCount;

    private PostPublicationState state;

    public Post(
        final Long id,
        final User author,
        final PostContent content
    ) {
        this(
            id,
            author,
            content,
            new PositiveIntegerCounter(),
            PostPublicationState.PUBLIC
        );
    }

    public Post(
        final Long id,
        final User author,
        final PostContent content,
        final PostPublicationState state
    ) {
        this(
            id,
            author,
            content,
            new PositiveIntegerCounter(),
            state
        );
    }

    public Post(
        final Long id,
        final User author,
        final PostContent content,
        final PositiveIntegerCounter likeCount,
        final PostPublicationState state
    ) {
        validateAuthor(author);

        this.id = id;
        this.author = author;
        this.content = content;
        this.likeCount = likeCount;
        this.state = state;
    }

    private void validateAuthor(final User author) {
        if (author == null) {
            throw new IllegalArgumentException();
        }
    }

    public void like(final User user) {
        if (this.author.equals(user)) {
            throw new IllegalArgumentException();
        }

        likeCount.increase();
    }

    public void unlike(final User user) {
        if (this.author.equals(user)) {
            throw new IllegalArgumentException();
        }

        likeCount.decrease();
    }

    public void updatePost(final User user, final String updateContent, final PostPublicationState updateState) {
        if (!author.equals(user)) {
            throw new IllegalArgumentException();
        }

        content.updateContent(updateContent);
        this.state = updateState;
    }

    public Long getId() {
        return id;
    }

    public int getLikeCount() {
        return likeCount.getCount();
    }

    public PostContent getContent() {
        return content;
    }

    public String getContentText() {
        return content.getContent();
    }

    public PostPublicationState getState() {
        return state;
    }

    public User getAuthor() {
        return author;
    }
}
