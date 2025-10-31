package site.study.post.domain.content;

public class CommentContent extends Content {

    public CommentContent(final String content) {
        super(content);
    }

    @Override
    protected void validateContent(final String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException();
        }

        if (content.length() > 100) {
            throw new IllegalArgumentException();
        }
    }
}
