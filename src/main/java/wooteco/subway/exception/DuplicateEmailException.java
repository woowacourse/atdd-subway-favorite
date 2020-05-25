package wooteco.subway.exception;

public class DuplicateEmailException extends RuntimeException {

    private static final String DUPLICATE_EMAIL_MESSAGE = "이미 사용하고 있는 이메일입니다.";

    public DuplicateEmailException() {
        super(DUPLICATE_EMAIL_MESSAGE);
    }
}
