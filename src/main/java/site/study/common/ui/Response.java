package site.study.common.ui;

import com.fasterxml.jackson.annotation.JsonInclude;
import site.study.common.domain.exception.ErrorCode;

public record Response<T>(
    Integer code,
    String message,
    @JsonInclude(JsonInclude.Include.NON_NULL) T value
) {
    public static <T> Response<T> ok(T value) {
        return new Response<>(0, "ok", value);
    }

    public static <T> Response<T> error(ErrorCode code) {
        return new Response<>(code.getCode(), code.getMessage(), null);
    }
}
