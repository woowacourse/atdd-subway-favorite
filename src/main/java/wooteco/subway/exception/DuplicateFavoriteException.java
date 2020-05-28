package wooteco.subway.exception;

// Todo: advice 추가
public class DuplicateFavoriteException extends RuntimeException {
    public DuplicateFavoriteException(String message) {
        super(message);
    }
}
