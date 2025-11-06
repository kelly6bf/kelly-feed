import { sleep, check } from 'k6';
import http from 'k6/http';
import {API_BASE_URL, parseResponseBody, setRequestHeader} from "../../../../common/api/api-common.js";

export const options = {
    setupTimeout: '30m',
    scenarios: {
        default: {
            executor: 'per-vu-iterations',
            vus: 400,
            iterations: 1,
            maxDuration: '30m',
        },
    },
};

export function setup() {
    console.log("⏰ 5초 대기 시작.");
    sleep(5);
    console.log("✅ 5초 대기 완료.\n");
}

export default function () {
    const vuIndex = __VU - 1;
    const response = requestApi(vuIndex);
    const responseBody = parseResponseBody(response);

    check(null, {
        'API 응답 상태 코드 200': () => response.status === 200,
        'API 코드 0': () => responseBody?.code === 0,
        'API 메시지 ok': () => responseBody?.message === 'ok',
        '응답 데이터 - 생성된 회원 ID 존재': () => responseBody?.value !== undefined,
    });
};

function requestApi(vuIndex) {
    const headers = setRequestHeader();
    const body = JSON.stringify({
        name: `user-${vuIndex}`,
        profileImageUrl : `https://image-${vuIndex}.site`
    });

    return http.post(`${API_BASE_URL}/api/user`, body, {headers});
}
