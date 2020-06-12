package wooteco.subway.exception;

public class NoSuchLineStationException extends SubwayException {
    public NoSuchLineStationException() {
        super("해당하는 구간이 없습니다.");
    }
}
