package wooteco.subway.web.exception.station;

public class StationException extends RuntimeException {
	public StationException() {
		super("예기치 못한 오류입니다.");
	}

	public StationException(String message) {
		super(message);
	}
}
