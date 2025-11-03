package site.study.user.domain;

import site.study.common.domain.PositiveIntegerCounter;

import java.util.Objects;

public class User {

    private final Long id;
    private final UserInfo userInfo;
    private final PositiveIntegerCounter followingCount;
    private final PositiveIntegerCounter followerCount;

    public User(final Long id, final UserInfo userInfo) {
        this(
            id,
            userInfo,
            new PositiveIntegerCounter(),
            new PositiveIntegerCounter()
        );
    }

    public User(
        final Long id,
        final UserInfo userInfo,
        final PositiveIntegerCounter followingCount,
        final PositiveIntegerCounter followerCount
    ) {
        validateUserInfo(userInfo);

        this.id = id;
        this.userInfo = userInfo;
        this.followingCount = followingCount;
        this.followerCount = followerCount;
    }

    private void validateUserInfo(final UserInfo userInfo) {
        if (userInfo == null) {
            throw new IllegalArgumentException();
        }
    }

    public void follow(final User targetUser) {
        if (targetUser.equals(this)) {
            throw new IllegalArgumentException();
        }

        followingCount.increase();
        targetUser.increaseFollowerCount();
    }

    private void increaseFollowerCount() {
        followerCount.increase();
    }

    public void unfollow(final User targetUser) {
        if (targetUser.equals(this)) {
            throw new IllegalArgumentException();
        }

        followingCount.decrease();
        targetUser.decreaseFollowerCount();
    }

    private void decreaseFollowerCount() {
        followerCount.decrease();
    }

    public Long getId() {
        return id;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public String getName() {
        return userInfo.getName();
    }

    public String getProfileImage() {
        return userInfo.getProfileImageUrl();
    }

    public int getFollowingCount() {
        return followingCount.getCount();
    }

    public int getFollowerCount() {
        return followerCount.getCount();
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
