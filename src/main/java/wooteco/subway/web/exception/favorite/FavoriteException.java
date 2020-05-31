package wooteco.subway.web.exception.favorite;

public class FavoriteException extends RuntimeException {
	public FavoriteException() {
		super("예기치 못한 오류입니다.");
	}

	public FavoriteException(String message) {
		super(message);
	}
}
