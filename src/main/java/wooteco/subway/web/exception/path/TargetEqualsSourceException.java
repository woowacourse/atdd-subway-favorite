package wooteco.subway.web.exception.path;

public class TargetEqualsSourceException extends PathException {
	public TargetEqualsSourceException() {
		super("출발역과 도착역이 같습니다.");
	}
}
