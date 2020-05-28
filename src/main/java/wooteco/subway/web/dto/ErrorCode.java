package wooteco.subway.web.dto;

public enum ErrorCode {
    SYSTEM_ERROR(100, "시스템 에러"),
    MEMBER_DUPLICATED_EMAIL(1000, "중복된 이메일 가입시도."),
    INVALID_EMAIL_FORMAT(1100, "이메일 형식이 잘못되었습니다."),
    UNSIGNED_EMAIL(1200, "등록되지 않은 이메일"),
    WRONG_PASSWORD(1300, "잘못된 패스워드"),
    INVALID_AUTHENTICATION(2000, "유효하지 않은 접근"),
    FAVORITE_DUPLICATED(3000, "이미 등록된 즐겨찾기"),
    FAVORITE_NOT_FOUND(3100, "등록되지 않은 즐겨찾기"),
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
