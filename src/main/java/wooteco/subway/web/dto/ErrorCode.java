package wooteco.subway.web.dto;

public enum ErrorCode {
    MEMBER_DUPLICATED_EMAIL(1000, "중복된 이메일 가입시도.");

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
