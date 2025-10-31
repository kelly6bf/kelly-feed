package site.study.post.domain.content;

public class PostContent extends Content {

    public PostContent(final String content) {
        super(content);
    }

    @Override
    protected void validateContent(final String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException();
        }

        if (content.length() < 5 || content.length() > 500) {
            throw new IllegalArgumentException();
        }
    }
}
