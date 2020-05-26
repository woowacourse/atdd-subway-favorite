package wooteco.subway.service.favorite;

public class ExistedFavoriteException extends RuntimeException {
    public ExistedFavoriteException() {
        super("이미 추가된 즐겨찾기 구간입니다.");
    }
}
