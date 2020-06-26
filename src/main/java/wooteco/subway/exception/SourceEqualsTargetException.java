package wooteco.subway.exception;

public class SourceEqualsTargetException extends IllegalArgumentException {
    public SourceEqualsTargetException() {
        super("출발역과 도착역이 같으면 안돼요.");
    }
}
