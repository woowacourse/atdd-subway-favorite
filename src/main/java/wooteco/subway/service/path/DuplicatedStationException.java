package wooteco.subway.service.path;

public class DuplicatedStationException extends RuntimeException {
    public DuplicatedStationException() {
        super("출발역과 도착역은 같을 수 없습니다.");
    }
}
