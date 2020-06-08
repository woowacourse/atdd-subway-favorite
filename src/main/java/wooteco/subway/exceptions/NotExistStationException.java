package wooteco.subway.exceptions;

public class NotExistStationException extends RuntimeException {
	public NotExistStationException(String name) {
		super(String.format("%s 이름을 가진 역은 존재하지 않습니다!", name));
	}

	public NotExistStationException(Long id) {
		super(String.format("%d 아이디를 가진 역은 존재하지 않습니다!", id));
	}
}
