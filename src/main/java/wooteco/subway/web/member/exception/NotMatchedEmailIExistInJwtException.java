package wooteco.subway.web.member.exception;

public class NotMatchedEmailIExistInJwtException extends InvalidAuthenticationException {
    public NotMatchedEmailIExistInJwtException(String message) {
        super(message + "Jwt 안의 이메일 값과 다릅니다.");
    }
}
