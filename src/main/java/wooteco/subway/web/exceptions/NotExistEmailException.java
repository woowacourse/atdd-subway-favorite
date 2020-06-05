package wooteco.subway.web.exceptions;

public class NotExistEmailException extends RuntimeException {
    public NotExistEmailException() {
        super("email이 존재하지 않습니다.");
    }
}
