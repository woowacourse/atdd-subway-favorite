package wooteco.subway.service.station;

/**
 *    class description
 *
 *    @author HyungJu An
 */
public class NotFoundStationException extends RuntimeException {
	public NotFoundStationException(final String message) {
		super(message);
	}
}
