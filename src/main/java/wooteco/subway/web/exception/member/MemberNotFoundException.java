package wooteco.subway.web.exception.member;

public class MemberNotFoundException extends MemberException {
	public MemberNotFoundException() {
		super("해당하는 회원이 존재하지 않습니다.");
	}
}
