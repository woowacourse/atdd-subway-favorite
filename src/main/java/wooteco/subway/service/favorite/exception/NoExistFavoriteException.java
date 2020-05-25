package wooteco.subway.service.favorite.exception;

public class NoExistFavoriteException extends RuntimeException {
    public NoExistFavoriteException() {
        super("존재하지 않는 즐겨찾기 입니다.");
    }
}
