package wooteco.subway.service.station;

public class StationNotFoundException extends RuntimeException {
	public StationNotFoundException(String message) {
		super("역을 찾을 수 없습니다. : " + message);
	}
}
