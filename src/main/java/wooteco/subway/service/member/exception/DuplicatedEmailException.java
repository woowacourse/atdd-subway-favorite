package wooteco.subway.service.member.exception;

public class DuplicatedEmailException extends MemberException {
	public DuplicatedEmailException() {
		super("해당 email을 가진 회원이 이미 존재합니다.");
	}
}
