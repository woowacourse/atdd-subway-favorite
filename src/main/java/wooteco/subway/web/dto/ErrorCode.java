package wooteco.subway.web.dto;

public enum ErrorCode {
    MEMBER_DUPLICATED_EMAIL(1000, "중복된 이메일 가입시도."),
    MEMBER_UNAUTHORIZED(2000, "허용되지 않은 접근"),
    TONEN_NOT_AUTHORIZED(2100, "토큰이 유효하지 않습니다."),
    TOKEN_NOT_FOUND(2110, "토큰 인증 정보가 존재하지 않습니다."),
    NOT_AUTHRIZED_EMAIL(2300, "허용되지 않은 정보 접근"),
    ;

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
