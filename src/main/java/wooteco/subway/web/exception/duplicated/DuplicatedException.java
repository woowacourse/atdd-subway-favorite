package wooteco.subway.web.exception.duplicated;

public class DuplicatedException extends RuntimeException {
	public DuplicatedException() {
		super("해당 자원이 이미 존재합니다.");
	}

	public DuplicatedException(String message) {
		super(message);
	}
}
