package wooteco.subway.web.member.exception;

// todo : 알맞은 에러 알아보기
public class NotExistStationDataException extends IllegalArgumentException {
    private static final String MESSAGE_TAIL = "를(을) 가진 데이터가 없습니다.";

    public NotExistStationDataException(String reason) {
        super(reason + MESSAGE_TAIL);
    }
}
