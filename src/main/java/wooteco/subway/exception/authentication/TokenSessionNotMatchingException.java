package wooteco.subway.exception.authentication;

public class TokenSessionNotMatchingException extends InvalidAuthenticationException {
    public TokenSessionNotMatchingException() {
        super("토큰과 세션 정보가 일치하지 않아요");
    }
}
