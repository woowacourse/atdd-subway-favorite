package wooteco.subway.web.member.exception;

public class NotExistException extends IllegalArgumentException {
    private static final String MESSAGE_TAIL = "에 해당하는 정보가 없습니다.";
    public NotExistException(String message) {
        super(message + MESSAGE_TAIL);
    }
}
