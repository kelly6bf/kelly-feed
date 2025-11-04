import fs from "fs";
import { writeToPath } from "fast-csv";

export async function createTestData() {
    // ===================== DB 테이블 데이터 =====================
    const community_user = [];
    const community_post = [];
    const community_comment = [];
    const community_user_relation = [];
    const community_like = [];
    const community_user_post_queue = [];

    // ===================== 상수 =====================
    const TOTAL_USER_COUNT = 1000;  // 총 회원 수
    const TOTAL_ACTIVITY_DATE = 365;  // 총 진행일

    // ===================== 메인 로직 =====================
    // 1. user 테이블 데이터 생성
    const userCreatedAt = getDateNDaysAgo(TOTAL_ACTIVITY_DATE - 1);
    for (let i = 0; i < TOTAL_USER_COUNT; i++) {
        const userId = i + 1;
        community_user.push({
            id: `${userId}`,
            name: `user-${userId}`,
            profile_image: `https://profile-image-${userId}.site`,
            follower_count: 249,
            following_count: 249,
            reg_dt: userCreatedAt,
            upd_dt: userCreatedAt
        });
    }

    // CSV 파일 생성
    const tables = [
        { name: "community_user", data: community_user },
    ];
    await Promise.all(
        tables.map(({name, data}) => writeCsv(`./csv/${name}.csv`, data))
    );
}

function getDateNDaysAgo(n) {
    const now = new Date();
    now.setDate(now.getDate() - n);
    return convertDateTimeFormatString(now);
}

function convertDateTimeFormatString(date = new Date()) {
    // MySQL DATETIME 형식으로 포맷 (YYYY-MM-DD HH:mm:ss)
    const pad = (n) => n.toString().padStart(2, '0');
    return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())} ${pad(date.getHours())}:${pad(date.getMinutes())}:${pad(date.getSeconds())}`;
}

function writeCsv(filePath, data) {
    return new Promise((resolve, reject) => {
        if (!data || data.length === 0) {
            console.log(`⚠️ 데이터 없음: ${filePath} 생략`);
            return resolve();
        }

        // 디렉토리 없으면 자동 생성
        const dir = filePath.substring(0, filePath.lastIndexOf("/"));
        if (!fs.existsSync(dir)) {
            fs.mkdirSync(dir, { recursive: true });
        }

        const csvStream = writeToPath(filePath, data, {
            headers: true,
            quoteColumns: true,
        });

        csvStream
            .on("finish", () => {
                console.log(`✅ CSV 생성 완료: ${filePath}`);
                resolve();
            })
            .on("error", (err) => {
                console.error(`❌ CSV 생성 오류 (${filePath}):`, err);
                reject(err);
            });
    });
}

createTestData()
    .then(() => console.log("✅ 모든 CSV 생성 완료"))
    .catch((err) => console.error("❌ CSV 생성 중 오류 발생:", err));
