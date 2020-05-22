package wooteco.subway.web.dto;

public enum ErrorCode {
    MEMBER_DUPLICATED_EMAIL(1000, "중복된 이메일 가입시도."),
    UNSIGNED_EMAIL(1100, "등록되지 않은 이메일"),
    WRONG_PASSWORD(1200, "잘못된 패스워드"),
    INVALID_AUTHENTICATION(2000, "유효하지 않은 접근");

    private final Integer code;
    private final String message;

    ErrorCode(final Integer code, final String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
