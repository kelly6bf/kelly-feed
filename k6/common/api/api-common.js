// API 공통 데이터
export const API_BASE_URL = "http://localhost:8090";

// API 공통 함수
export function setRequestHeader() {
    return {
        'Content-Type': 'application/json'
    };
}

export function parseResponseBody(response) {
    return JSON.parse(response.body);
}
