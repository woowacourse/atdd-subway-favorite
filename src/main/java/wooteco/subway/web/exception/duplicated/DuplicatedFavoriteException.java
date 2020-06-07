package wooteco.subway.web.exception.duplicated;

public class DuplicatedFavoriteException extends DuplicatedException {
	public DuplicatedFavoriteException() {
		super("이미 존재하는 Favorite입니다.");
	}
}
