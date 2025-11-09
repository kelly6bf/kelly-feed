// ===================== 상수 =====================
const TOTAL_USER_COUNT = 400;  // 총 회원 수
const TOTAL_ACTIVITY_DATE = 365;  // 총 진행일
const ONE_DAY_POST_COUNT_PER_USER = 10;

export function getTargetPostList(userCount) {
    const targetPostList = [];
    for (let i = 0; i < userCount; i++) {
        targetPostList.push(getFirstPostIdByDayAndUser(0, i + 1));
    }

    return targetPostList;
}

export function getFirstPostIdByDayAndUser(dayIndex, userId) {
    return (
        ((TOTAL_ACTIVITY_DATE - 1 - dayIndex) * TOTAL_USER_COUNT * ONE_DAY_POST_COUNT_PER_USER) +
        ((userId - 1) * ONE_DAY_POST_COUNT_PER_USER) +
        1
    );
}
