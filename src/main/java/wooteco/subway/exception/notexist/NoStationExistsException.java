package wooteco.subway.exception.notexist;

public class NoStationExistsException extends NoResourceExistException {
    public NoStationExistsException() {
        super("해당역이 존재하지 않아요.");
    }
}
