package wooteco.subway.web.exception.favorite;

public class DuplicatedFavoriteException extends FavoriteException {
	public DuplicatedFavoriteException() {
		super("이미 존재하는 Favorite입니다.");
	}
}
