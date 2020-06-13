package wooteco.subway.service.favorite.exception;

public class DuplicateFavoriteException extends RuntimeException {
    public DuplicateFavoriteException() {
        super("이미 존재하는 즐겨찾기 입니다.");
    }
}

