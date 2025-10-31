package site.study.user.domain;

public class UserInfo {

    private final String name;
    private final String profileImageUrl;

    public UserInfo(final String name, final String profileImageUrl) {
        validateName(name);

        this.name = name;
        this.profileImageUrl = profileImageUrl;
    }

    private void validateName(final String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException();
        }
    }

    public String getName() {
        return name;
    }
}
