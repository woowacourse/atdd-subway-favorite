package wooteco.subway.exception.notexist;

public class NoLineExistException extends NoResourceExistException {
    public NoLineExistException() {
        super("해당 노선은 존재하지 않아요.");
    }
}
