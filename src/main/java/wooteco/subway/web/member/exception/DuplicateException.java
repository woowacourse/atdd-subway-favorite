package wooteco.subway.web.member.exception;

public class DuplicateException extends IllegalArgumentException {
    private static final String MESSAGE = " : 중복된 값입니다.";

    public DuplicateException(String message) {
        super(message + MESSAGE);
    }
}
