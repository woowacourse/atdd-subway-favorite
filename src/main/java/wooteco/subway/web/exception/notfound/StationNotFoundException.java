package wooteco.subway.web.exception.notfound;

public class StationNotFoundException extends NotFoundException {
	public StationNotFoundException() {
		super("해당하는 역이 존재하지 않습니다.");
	}
}
