package wooteco.subway.exceptions;

public class NotExistFavoritePathException extends RuntimeException {
	public NotExistFavoritePathException(Long pathId) {
		super(String.format("Id가 %d인 즐겨찾기 경로가 존재하지 않습니다.", pathId));
	}
}
