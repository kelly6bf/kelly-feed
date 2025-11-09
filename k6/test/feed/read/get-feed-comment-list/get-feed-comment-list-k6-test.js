import { sleep, check } from 'k6';
import http from 'k6/http';
import {API_BASE_URL, parseResponseBody, setRequestHeader} from "../../../../common/api/api-common.js";
import {getTargetPostList} from "../../../../common/util/util.js";

// N명의 VUser가 동시에 요청
export const options = {
    setupTimeout: '30m',
    scenarios: {
        default: {
            executor: 'per-vu-iterations',
            vus: 100,
            iterations: 1,
            maxDuration: '30m',
        },
    },
};

// N명의 VUser가 점진적으로 요청
// export const options = {
//     scenarios: {
//         ramping_test: {
//             executor: 'ramping-vus',
//             startVUs: 10,            // 시작 VU 수
//             stages: [
//                 { duration: '2m', target: 100 },  // 2분 동안 100명까지 증가
//                 { duration: '1m', target: 100 }, // 10분간 100명 유지
//                 { duration: '2m', target: 0 },    // 5분 동안 0명으로 감소
//             ],
//             gracefulRampDown: '30s',  // 종료 시 30초 동안 부드럽게 종료
//             exec: 'default',           // 실행할 함수 (Kelly의 default 함수)
//         },
//     },
//     thresholds: {
//         http_req_duration: ['p(95)<1000'], // 95% 요청이 1초 이내에 완료되어야 함
//     },
// };

export function setup() {
    const targetPostList = getTargetPostList(100);
    console.log("⏰ 5초 대기 시작.");
    sleep(5);
    console.log("✅ 5초 대기 완료.\n");

    return {targetPostList};
}

export default function (data) {
    const vuIndex = __VU - 1;
    const targetPostId = data.targetPostList[vuIndex];
    const response = requestApi(vuIndex, targetPostId);
    const responseBody = parseResponseBody(response);

    check(null, {
        'API 응답 상태 코드 200': () => response.status === 200,
        'API 코드 0': () => responseBody?.code === 0,
        'API 메시지 ok': () => responseBody?.message === 'ok',
        '응답 데이터 - 피드 댓글 리스트 배열 존재': () => Array.isArray(responseBody?.value),
        '응답 데이터 - 피드 댓글 리스트 배열 비어있지 않음': () => (responseBody?.value?.length ?? 0) > 0,
        '응답 데이터 - 피드 댓글 ID 존재': () => responseBody?.value?.[0]?.id !== undefined,
        '응답 데이터 - 피드 댓글 본문 존재': () => responseBody?.value?.[0]?.content !== undefined,
        '응답 데이터 - 피드 댓글 작성자 ID 존재': () => responseBody?.value?.[0]?.userId !== undefined,
        '응답 데이터 - 피드 댓글 작성자 이름 존재': () => responseBody?.value?.[0]?.userName !== undefined,
        '응답 데이터 - 피드 댓글 작성자 프로필 이미지 URL 존재': () => responseBody?.value?.[0]?.userProfileImage !== undefined,
        '응답 데이터 - 피드 댓글 생성일 존재': () => responseBody?.value?.[0]?.createdAt !== undefined,
        '응답 데이터 - 피드 댓글 수정일 존재': () => responseBody?.value?.[0]?.updatedAt !== undefined,
        '응답 데이터 - 피드 댓글 좋아요 수 존재': () => responseBody?.value?.[0]?.likeCount !== undefined,
        '응답 데이터 - 내가 피드 댓글에 좋아요 눌렀는지 여부 존재': () => responseBody?.value?.[0]?.likedByMe !== undefined,
    });

    sleep(1);
};

function requestApi(vuIndex, targetPostId) {
    const headers = setRequestHeader();

    return http.get(`${API_BASE_URL}/api/feed/${targetPostId}/comments?userId=${vuIndex + 1}`, {headers});
}
