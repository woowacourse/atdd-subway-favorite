package wooteco.subway.service.exception;

public class SameStationException extends RuntimeException {
    public SameStationException() {
        this("SAME_STATION");
    }

    public SameStationException(String message) {
        super(message);
    }
}
