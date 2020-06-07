package wooteco.subway.web.exception.path;

public class PathException extends RuntimeException {
	public PathException() {
		super("예기치 못한 오류입니다.");
	}

	public PathException(String message) {
		super(message);
	}
}
