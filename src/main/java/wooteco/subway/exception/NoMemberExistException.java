package wooteco.subway.exception;

public class NoMemberExistException extends NoResourceExistException {
    public NoMemberExistException() {
        super("해당 회원은 존재하지 않아요.");
    }
}
