package wooteco.subway.service.member;

public class DuplicatedEmailException extends RuntimeException {
	public DuplicatedEmailException(String message) {
		super(message + "는 중복된 이메일 입니다.");
	}
}
