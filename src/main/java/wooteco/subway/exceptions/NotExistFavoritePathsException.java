package wooteco.subway.exceptions;

public class NotExistFavoritePathsException extends RuntimeException {
	public NotExistFavoritePathsException() {
		super("등록된 즐겨찾기 경로가 없습니다!");
	}
}
