package wooteco.subway.exceptions;

public class PasswordConflictException extends RuntimeException {
	public PasswordConflictException() {
		super("비밀번호와 비밀번호 확인란에 적은 비밀번호가 다르면 안됩니다!");
	}
}
