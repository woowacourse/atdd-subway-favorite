package wooteco.subway.web.exception.notfound;

public class MemberNotFoundException extends NotFoundException {
	public MemberNotFoundException() {
		super("해당하는 회원이 존재하지 않습니다.");
	}
}
