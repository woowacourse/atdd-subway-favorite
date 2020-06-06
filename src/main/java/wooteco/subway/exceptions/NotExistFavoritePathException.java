package wooteco.subway.exceptions;

public class NotExistFavoritePathException extends ResourceNotExistException {
    public NotExistFavoritePathException(Long pathId) {
        super(String.format("Id가 %d인 즐겨찾기 경로가 존재하지 않습니다.", pathId));
    }

    public NotExistFavoritePathException() {
        super("출발역과 도착역이 같은 경로는 존재하지 않습니다.");
    }
}
