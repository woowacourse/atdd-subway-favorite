package wooteco.subway.web.member;

public class InvalidTokenException extends BadRequestException {
    public InvalidTokenException() {
        super("유효하지 않은 토큰값입니다.");
    }
}
