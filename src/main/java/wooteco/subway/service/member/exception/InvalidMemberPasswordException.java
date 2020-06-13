package wooteco.subway.service.member.exception;

public class InvalidMemberPasswordException extends RuntimeException {
    public InvalidMemberPasswordException() {
        super("회원 정보가 일치하지 않습니다.");
    }
}
