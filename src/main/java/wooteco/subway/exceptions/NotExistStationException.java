package wooteco.subway.exceptions;

public class NotExistStationException extends ResourceNotExistException {
    public NotExistStationException(String name) {
        super(String.format("%s 이름을 가진 역은 존재하지 않습니다!", name));
    }

    public NotExistStationException(Long id) {
        super(String.format("Id가 %d인 역은 존재하지 않습니다!", id));
    }
}
