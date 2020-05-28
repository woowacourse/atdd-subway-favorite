package wooteco.subway.service.favorite.exception;

public class TargetEqualsSourceException extends FavoriteException {
	public TargetEqualsSourceException() {
		super("출발역과 도착역이 같습니다.");
	}
}
