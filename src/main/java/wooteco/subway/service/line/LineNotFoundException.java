package wooteco.subway.service.line;

public class LineNotFoundException extends RuntimeException {
	public LineNotFoundException(String message) {
		super("노선이 존재하지 않습니다. :" + message);
	}
}
