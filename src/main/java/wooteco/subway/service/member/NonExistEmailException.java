package wooteco.subway.service.member;

public class NonExistEmailException extends RuntimeException {
	public NonExistEmailException(String message) {
		super("존재하지않는 이메일 입니다. :" + message);
	}
}
