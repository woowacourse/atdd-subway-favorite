package wooteco.subway.domain.favorite.exception;

public class DuplicatedFavoriteException extends RuntimeException {
    public DuplicatedFavoriteException(String message) {
        super(message);
    }
}
