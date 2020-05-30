package wooteco.subway.service.member;

public class MemberNotFoundException extends RuntimeException {
	public MemberNotFoundException(String message) {
		super("회원을 찾을 수 없습니다. : " + message);
	}

}
