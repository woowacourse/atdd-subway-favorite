package wooteco.subway.exception;

public class DuplicatedEmailException extends IllegalArgumentException {
	public DuplicatedEmailException() {
		super("이미 존재하는 이메일이에요.");
	}
}
