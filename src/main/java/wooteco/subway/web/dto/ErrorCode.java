package wooteco.subway.web.dto;

public enum ErrorCode {
    INVALID_INPUT("유효한 입력이 아닙니다."),
    DUPLICATE_MEMBER_EMAIL("이미 가입된 이메일 입니다."),
    INCORRECT_PASSWORD("비밀번호가 일치하지 않습니다."),
    NOT_FOUND_MEMBER("존재하지 않는 회원입니다."),
    INVALID_AUTH_TOKEN("유효하지 않는 토큰입니다."),
    SERVER_ERROR("서버에서 오류가 발생했습니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
