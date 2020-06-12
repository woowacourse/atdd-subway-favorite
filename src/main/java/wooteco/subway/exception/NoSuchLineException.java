package wooteco.subway.exception;

public class NoSuchLineException extends SubwayException {
    public NoSuchLineException() {
        super("해당하는 노선이 없습니다.");
    }
}
