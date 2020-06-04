package wooteco.subway.exception;

public class InvalidFavoriteException extends RuntimeException{
	public static final String NOT_FOUND_FAVORITE = "존재하지 않는 favorite입니다.";

	public InvalidFavoriteException(String message) {
		super(message);
	}
}
