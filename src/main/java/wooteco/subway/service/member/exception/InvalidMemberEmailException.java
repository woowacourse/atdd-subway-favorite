package wooteco.subway.service.member.exception;

public class InvalidMemberEmailException extends RuntimeException {
    public InvalidMemberEmailException() {
        super("회원 정보가 일치하지 않습니다.");
    }
}
