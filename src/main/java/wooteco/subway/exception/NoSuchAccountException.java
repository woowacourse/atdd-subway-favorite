package wooteco.subway.exception;

public class NoSuchAccountException extends SubwayException {
	public NoSuchAccountException() {
		super("해당 이메일의 계정이 존재하지 않습니다.");
	}
}
