package site.study.post.domain.content;

import site.study.post.domain.common.DatetimeInfo;

public abstract class Content {

    protected final DatetimeInfo datetimeInfo;

    protected String content;

    protected Content(final String content) {
        validateContent(content);
        this.datetimeInfo = new DatetimeInfo();
        this.content = content;
    }

    public void updateContent(final String updateContent) {
        validateContent(updateContent);
        this.content = updateContent;
        datetimeInfo.updateEditDatetime();
    }

    protected abstract void validateContent(String content);

    public boolean isEdited() {
        return datetimeInfo.isEdited();
    }

    public String getContent() {
        return content;
    }
}
