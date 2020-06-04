package wooteco.subway.service.member.exception;

public class IncorrectPasswordException extends RuntimeException {
    public static final String INCORRECT_PASSWORD_EXCEPTION_MESSAGE = "비밀번호가 일치하지 않습니다.";

    public IncorrectPasswordException() {
        super(INCORRECT_PASSWORD_EXCEPTION_MESSAGE);
    }
}
