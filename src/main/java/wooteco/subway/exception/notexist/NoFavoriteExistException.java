package wooteco.subway.exception.notexist;

public class NoFavoriteExistException extends NoResourceExistException {
    public NoFavoriteExistException() {
        super("해당 즐겨찾기는 존재하지 않아요.");
    }
}
