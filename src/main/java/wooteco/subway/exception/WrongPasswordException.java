package wooteco.subway.exception;

public class WrongPasswordException extends SubwayException {
    public WrongPasswordException() {
        super("잘못된 패스워드입니다.");
    }
}
