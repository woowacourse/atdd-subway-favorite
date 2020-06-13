package wooteco.subway.service.member.exception;

public class InvalidMemberIdException extends RuntimeException {
    public InvalidMemberIdException() {
        super("아이디에 일치하는 회원이 없습니다.");
    }
}
