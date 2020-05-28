package wooteco.subway.service.favorite.exception;

public class StationNotFoundException extends FavoriteException {
	public StationNotFoundException() {
		super("해당하는 역이 존재하지 않습니다.");
	}
}
