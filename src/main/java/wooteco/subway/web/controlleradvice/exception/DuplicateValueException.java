package wooteco.subway.web.controlleradvice.exception;

public class DuplicateValueException extends IllegalArgumentException {
    private static final String FORMATTED_ERROR_MESSAGE = "%s : 이미 존재하는 이름입니다.";

    public DuplicateValueException(String name) {
        super(String.format(FORMATTED_ERROR_MESSAGE, name));
    }
}
