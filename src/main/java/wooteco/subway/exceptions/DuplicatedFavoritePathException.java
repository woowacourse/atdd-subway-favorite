package wooteco.subway.exceptions;

public class DuplicatedFavoritePathException extends RuntimeException {
	public DuplicatedFavoritePathException() {
		super("이미 등록된 즐겨찾기 경로입니다!");
	}
}
