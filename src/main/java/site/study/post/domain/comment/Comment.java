package site.study.post.domain.comment;

import site.study.common.domain.PositiveIntegerCounter;
import site.study.post.domain.Post;
import site.study.post.domain.content.CommentContent;
import site.study.user.domain.User;

public class Comment {

    private final Long id;
    private final Post post;
    private final User author;
    private final CommentContent content;
    private final PositiveIntegerCounter likeCount;

    public Comment(
        final Long id,
        final Post post,
        final User author,
        final CommentContent content
    ) {
        validatePost(post);
        validateAuthor(author);
        validateContent(content);

        this.id = id;
        this.post = post;
        this.author = author;
        this.content = content;
        this.likeCount = new PositiveIntegerCounter();
    }

    private void validatePost(final Post post) {
        if (post == null) {
            throw new IllegalArgumentException();
        }
    }

    private void validateAuthor(final User author) {
        if (author == null) {
            throw new IllegalArgumentException();
        }
    }

    private void validateContent(final CommentContent content) {
        if (content == null) {
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

    public void updateComment(final User user, final String updateContent) {
        if (!author.equals(user)) {
            throw new IllegalArgumentException();
        }

        content.updateContent(updateContent);
    }

    public Long getId() {
        return id;
    }

    public Post getPost() {
        return post;
    }

    public User getAuthor() {
        return author;
    }

    public CommentContent getContent() {
        return content;
    }

    public int getLikeCount() {
        return likeCount.getCount();
    }

    public String getContentText() {
        return content.getContent();
    }
}
