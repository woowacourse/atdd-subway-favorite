package wooteco.subway.web.exception;

public class NotFoundFavoriteException extends AuthenticationException {
    public static final String ERROR_MESSAGE = " 즐겨찾기를 찾을 수 없습니다.";

    public NotFoundFavoriteException() {
        super(ERROR_MESSAGE);
    }

    public NotFoundFavoriteException(Long id) {
        super(id + ERROR_MESSAGE);
    }
}
