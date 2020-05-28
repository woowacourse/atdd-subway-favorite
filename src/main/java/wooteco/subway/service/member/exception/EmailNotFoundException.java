package wooteco.subway.service.member.exception;

public class EmailNotFoundException extends MemberException {
	public EmailNotFoundException() {
		super("해당 이메일이 존재하지 않습니다.");
	}
}
