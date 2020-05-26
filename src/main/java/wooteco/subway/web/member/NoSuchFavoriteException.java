package wooteco.subway.web.member;

public class NoSuchFavoriteException extends RuntimeException {
    public static final String NO_SUCH_FAVORITE_EXCEPTION_MESSAGE = "해당하는 즐겨찾기를 찾을 수 없습니다.";

    public NoSuchFavoriteException() {
        super(NO_SUCH_FAVORITE_EXCEPTION_MESSAGE);
    }

}
