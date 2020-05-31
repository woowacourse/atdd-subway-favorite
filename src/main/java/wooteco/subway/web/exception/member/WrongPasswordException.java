package wooteco.subway.web.exception.member;

public class WrongPasswordException extends MemberException {
	public WrongPasswordException() {
		super("패스워드가 일치하지 않습니다.");
	}
}
