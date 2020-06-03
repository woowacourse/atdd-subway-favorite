package wooteco.subway.exception;

public class AlreadyExistsEmailException extends RuntimeException {

    private static final String ALREADY_EXISTS_EMAIL_MESSAGE = "이미 사용하고 있는 이메일입니다.";

    public AlreadyExistsEmailException() {
        super(ALREADY_EXISTS_EMAIL_MESSAGE);
    }
}
