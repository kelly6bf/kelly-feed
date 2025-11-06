import fs from "fs";
import {format} from "fast-csv";

// ===================== 상수 =====================
const TOTAL_USER_COUNT = 400;  // 총 회원 수
const USER_COUNT_PER_GROUP = 20;
const TOTAL_ACTIVITY_DATE = 365;  // 총 진행일
const ONE_DAY_POST_COUNT_PER_USER = 10;

// ===================== 캐싱 =====================
const CREATED_POST_CACHING = new Map();  // [생성일][유저 ID]

export async function createTestData() {
    // ===================== DB 테이블 CSV Stream =====================
    const community_user_stream = format({ headers: true });
    community_user_stream.pipe(fs.createWriteStream("./csv/community_user.csv"));

    const community_user_relation_stream = format({ headers: true });
    community_user_relation_stream.pipe(fs.createWriteStream("./csv/community_user_relation.csv"));

    const community_post_stream = format({ headers: true });
    community_post_stream.pipe(fs.createWriteStream("./csv/community_post.csv"));

    const community_user_post_queue_stream = format({ headers: true });
    community_user_post_queue_stream.pipe(fs.createWriteStream("./csv/community_user_post_queue.csv"));

    const community_comment_stream = format({ headers: true });
    community_comment_stream.pipe(fs.createWriteStream("./csv/community_comment.csv"));

    const community_like_stream = format({ headers: true });
    community_like_stream.pipe(fs.createWriteStream("./csv/community_like.csv"));

    // ===================== 메인 로직 =====================
    // 1. user 테이블 데이터 생성
    console.log("🧑‍🍳 community_user 테이블 테스트 데이터 생성중...");
    const userCreatedAt = getDateNDaysAgo(TOTAL_ACTIVITY_DATE - 1);
    for (let i = 0; i < TOTAL_USER_COUNT; i++) {
        const userId = i + 1;
        community_user_stream.write({
            id: `${userId}`,
            name: `user-${userId}`,
            profile_image: `https://profile-image-${userId}.site`,
            follower_count: USER_COUNT_PER_GROUP - 1,
            following_count: USER_COUNT_PER_GROUP - 1,
            reg_dt: userCreatedAt,
            upd_dt: userCreatedAt
        });
    }
    console.log("✅ community_user 테이블 테스트 데이터 생성 완료!\n");

    // 2. user_relation 테이블 데이터 생성
    console.log("🧑‍🍳 community_user_relation 테이블 테스트 데이터 생성중...");
    for (let userId = 1; userId <= TOTAL_USER_COUNT; userId++) {
        const startOtherUserId = getFirstUserIdInGroup(userId);
        for (let otherUserId = startOtherUserId; otherUserId <= (startOtherUserId + USER_COUNT_PER_GROUP - 1); otherUserId++) {
            if (otherUserId === userId) {
                continue;
            }

            community_user_relation_stream.write({
                follower_user_id: otherUserId,
                following_user_id: userId,
                reg_dt: userCreatedAt,
                upd_dt: userCreatedAt
            });
        }
    }
    console.log("✅ community_user_relation 테이블 테스트 데이터 생성 완료!\n");

    // 3. post & user_post_queue 테이블 데이터 생성
    console.log("🧑‍🍳 community_post & user_post_queue 테이블 테스트 데이터 생성중...");
    let postId = 1;
    let userPostQueueId = 1;
    for (let dayIndex = TOTAL_ACTIVITY_DATE - 1; dayIndex >= 0; dayIndex--) {
        const createPostAt = getDateNDaysAgo(dayIndex);
        for (let userId = 1; userId <= TOTAL_USER_COUNT; userId++) {
            for (let j = 0; j < ONE_DAY_POST_COUNT_PER_USER; j++) {
                const currentPostId = postId++;

                // 생성된 게시글 캐싱
                if (j === 0) {
                    CREATED_POST_CACHING.set(`${dayIndex}-${userId}`, currentPostId);
                }

                community_post_stream.write({
                    id: currentPostId,
                    author_id: userId,
                    content: `게시글 본문! - ${currentPostId}`,
                    state: "PUBLIC",
                    comment_count: 0,
                    like_count: 0,
                    reg_dt: createPostAt,
                    upd_dt: createPostAt
                });

                const startOtherUserId = getFirstUserIdInGroup(userId);
                for (let otherUserId = startOtherUserId; otherUserId <= (startOtherUserId + USER_COUNT_PER_GROUP - 1); otherUserId++) {
                    if (otherUserId === userId) {
                        continue;
                    }

                    community_user_post_queue_stream.write({
                        id: userPostQueueId++,
                        author_id: userId,
                        post_id: currentPostId,
                        user_id: otherUserId,
                        reg_dt: userCreatedAt,
                        upd_dt: userCreatedAt
                    });
                }
            }
        }
    }
    console.log("✅ community_post & user_post_queue 테이블 테스트 데이터 생성 완료!\n");

    // 4. comment & like 테이블 데이터 생성
    console.log("🧑‍🍳 community_post & user_post_queue 테이블 테스트 데이터 생성중...");
    let commentId = 1;
    for (let dayIndex = TOTAL_ACTIVITY_DATE - 1; dayIndex >= 0; dayIndex--) {
        const createCommentAt = getDateNDaysAgo(dayIndex);
        const createLikeAt = getDateNDaysAgo(dayIndex);

        for (let userId = 1; userId <= TOTAL_USER_COUNT; userId++) {
            const startOtherUserId = getFirstUserIdInGroup(userId);
            for (let otherUserId = startOtherUserId; otherUserId <= (startOtherUserId + USER_COUNT_PER_GROUP - 1); otherUserId++) {
                if (otherUserId === userId) {
                    continue;
                }

                const targetPostId = CREATED_POST_CACHING.get(`${dayIndex}-${otherUserId}`);
                community_comment_stream.write({
                    id: commentId++,
                    post_id: targetPostId,
                    author_id: userId,
                    content: "게시글 댓글!!",
                    like_count: 0,
                    reg_dt: createCommentAt,
                    upd_dt: createCommentAt
                });

                community_like_stream.write({
                    target_id: targetPostId,
                    target_type: "POST",
                    user_id: userId,
                    reg_dt: createLikeAt,
                    upd_dt: createLikeAt
                });
            }
        }
    }

    // CSV Stream 정리
    console.log("🧹 CSV Stream 정리중...");
    community_user_stream.end();
    community_user_relation_stream.end();
    community_post_stream.end();
    community_user_post_queue_stream.end();
    community_comment_stream.end();
    community_like_stream.end();
    console.log("✅ CSV Stream 정리 완료!\n");
}

function getDateNDaysAgo(n) {
    const now = new Date();
    now.setDate(now.getDate() - n);
    return convertDateTimeFormatString(now);
}

function getFirstUserIdInGroup(userId) {
    return getUserGroupIndex(userId) * USER_COUNT_PER_GROUP + 1;
}

function getUserGroupIndex(userId) {
    return Math.floor((userId - 1) / USER_COUNT_PER_GROUP);
}

function convertDateTimeFormatString(date = new Date()) {
    // MySQL DATETIME 형식으로 포맷 (YYYY-MM-DD HH:mm:ss)
    const pad = (n) => n.toString().padStart(2, '0');
    return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())} ${pad(date.getHours())}:${pad(date.getMinutes())}:${pad(date.getSeconds())}`;
}

createTestData()
    .then(() => console.log("🎉 모든 CSV 파일 생성 완료!"))
    .catch((err) => console.error("❌ CSV 생성 중 오류 발생 : ", err));
