package wooteco.subway.web.exception.notfound;

public class FavoriteNotFoundException extends NotFoundException {
	public FavoriteNotFoundException() {
		super("해당하는 즐겨찾기가 없습니다.");
	}
}
