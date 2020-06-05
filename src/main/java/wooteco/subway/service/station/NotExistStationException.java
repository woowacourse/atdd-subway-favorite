package wooteco.subway.service.station;

public class NotExistStationException extends RuntimeException {
    public NotExistStationException() {
        super("존재하지 않는 역입니다.");
    }
}
