package wooteco.subway.web.exception;

public class NotMatchMemberException extends AuthenticationException {
    private static final String ERROR_MESSAGE = "회원이 일치하지 않습니다.";

    public NotMatchMemberException() {
        super(ERROR_MESSAGE);
    }
}
