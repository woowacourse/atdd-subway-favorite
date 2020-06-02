package wooteco.subway.exception.notexist;

public class NoPathExistsException extends NoResourceExistException {
    public NoPathExistsException() {
        super("해당 경로는 존재하지 않아요.");
    }
}
