package wooteco.subway.web.exception;

public class DuplicatedFavoriteException extends BusinessException {
    public DuplicatedFavoriteException(Long sourceId, Long targetId) {
        super(sourceId + "번 역에서 " + targetId + "번 역까지 가는 경로는 이미 추가하셨습니다.");
    }
}
