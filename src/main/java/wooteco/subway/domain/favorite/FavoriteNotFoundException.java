package wooteco.subway.domain.favorite;

import wooteco.subway.exception.SubwayException;

public class FavoriteNotFoundException extends SubwayException {
	public FavoriteNotFoundException() {
		super("찾을 수 없는 즐겨찾기 입니다.");
	}
}
