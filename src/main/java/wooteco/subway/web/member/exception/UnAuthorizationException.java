package wooteco.subway.web.member.exception;


public class UnAuthorizationException extends RuntimeException {
    public UnAuthorizationException() {
        super("권한 없는 요청입니다");
    }
}
