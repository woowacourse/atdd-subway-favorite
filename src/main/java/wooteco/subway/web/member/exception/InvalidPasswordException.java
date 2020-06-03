package wooteco.subway.web.member.exception;

public class InvalidPasswordException extends IllegalArgumentException {
    private static final String MESSAGE = "잘못된 패스워드";

    public InvalidPasswordException() {
        super(MESSAGE);
    }
}
