package wooteco.subway.service.path;

public class NotExistedStationException extends RuntimeException {
    public NotExistedStationException() {
        super("존재하지 않는 역입니다.");
    }
}
