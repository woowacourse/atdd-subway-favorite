package wooteco.subway.exception;

public class FavoriteNotFoundException extends EntityNotFoundException {
    private static final String EXCEPTION_MESSAGE = "즐겨찾기 경로가 존재하지 않습니다.";

    public FavoriteNotFoundException() {
        super(EXCEPTION_MESSAGE);
    }
}
