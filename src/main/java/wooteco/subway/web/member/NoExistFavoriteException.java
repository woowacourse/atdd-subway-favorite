package wooteco.subway.web.member;

public class NoExistFavoriteException extends BadRequestException {

    public static final String NO_EXIST_FAVORITE_MSG = "존재하지 않는 즐겨찾기입니다.";

    public NoExistFavoriteException() {
        super(NO_EXIST_FAVORITE_MSG);
    }
}
