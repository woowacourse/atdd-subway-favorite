package wooteco.subway.service.member;

public class WrongPasswordException extends RuntimeException {
	public WrongPasswordException() {
		super("잘못된 패스워드입니다.");
	}
}
