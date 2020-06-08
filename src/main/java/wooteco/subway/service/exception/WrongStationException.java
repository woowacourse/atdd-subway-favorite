package wooteco.subway.service.exception;

public class WrongStationException extends RuntimeException {
    public WrongStationException() {
        this("WRONG_STATION");
    }

    public WrongStationException(String message) {
        super(message);
    }
}
