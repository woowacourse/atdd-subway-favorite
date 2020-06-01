package wooteco.subway.web.exception.notfound;

public class NotFoundException extends RuntimeException {
	public NotFoundException() {
		super("해당 자원이 존재하지 않습니다.");
	}

	public NotFoundException(String message) {
		super(message);
	}
}
