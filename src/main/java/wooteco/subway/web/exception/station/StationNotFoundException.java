package wooteco.subway.web.exception.station;

public class StationNotFoundException extends StationException {
	public StationNotFoundException() {
		super("해당하는 역이 존재하지 않습니다.");
	}
}
