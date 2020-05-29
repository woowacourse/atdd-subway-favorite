package wooteco.subway.exception;

public class NoLineStationExistsException extends NoResourceExistException {
    public NoLineStationExistsException() {
        super("존재하지 않는 구간이에요.");
    }
}