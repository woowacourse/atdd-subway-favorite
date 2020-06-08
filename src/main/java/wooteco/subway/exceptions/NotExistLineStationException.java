package wooteco.subway.exceptions;

public class NotExistLineStationException extends ResourceNotExistException {
    public NotExistLineStationException(Long preStationId, Long stationId) {
        super(String.format("%d-%d의 역 id를 갖는 구간이 존재하지 않습니다.", preStationId, stationId));
    }
}
