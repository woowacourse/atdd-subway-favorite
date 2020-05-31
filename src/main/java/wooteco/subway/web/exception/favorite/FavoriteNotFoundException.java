package wooteco.subway.web.exception.favorite;

public class FavoriteNotFoundException extends FavoriteException {
	public FavoriteNotFoundException() {
		super("해당하는 즐겨찾기가 없습니다.");
	}
}
