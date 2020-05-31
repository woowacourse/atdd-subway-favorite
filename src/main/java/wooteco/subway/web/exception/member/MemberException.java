package wooteco.subway.web.exception.member;

public class MemberException extends RuntimeException {
	public MemberException() {
		super("예기치 못한 오류입니다.");
	}

	public MemberException(String message) {
		super(message);
	}
}
