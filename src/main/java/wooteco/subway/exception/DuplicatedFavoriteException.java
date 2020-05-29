package wooteco.subway.exception;

public class DuplicatedFavoriteException extends IllegalArgumentException {
	public DuplicatedFavoriteException() {
		super("이미 추가된 즐겨찾기에요.");
	}
}
