package wooteco.subway.web.member.exception;

public class NotExistStationDataException extends NotExistDataException {
    private static final String MESSAGE_TAIL = "를(을) 가진 데이터가 없습니다.";

    public NotExistStationDataException(String reason) {
        super(reason + MESSAGE_TAIL);
    }
}
