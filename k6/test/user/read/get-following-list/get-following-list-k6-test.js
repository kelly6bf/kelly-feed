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
        '응답 데이터 - 필로잉 리스트 배열 존재': () => Array.isArray(responseBody?.value),
        '응답 데이터 - 배열 비어있지 않음': () => (responseBody?.value?.length ?? 0) > 0,
        '응답 데이터 - 회원 이름 존재': () => responseBody?.value?.[0]?.name !== undefined,
        '응답 데이터 - 회원 프로필 이미지 URL 존재': () => responseBody?.value?.[0]?.profileImage !== undefined,
    });
};

function requestApi(vuIndex) {
    const headers = setRequestHeader();

    return http.get(`${API_BASE_URL}/api/user/${vuIndex + 1}/following`, {headers});
}
