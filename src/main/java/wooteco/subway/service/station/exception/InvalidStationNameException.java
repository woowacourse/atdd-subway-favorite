package wooteco.subway.service.station.exception;

public class InvalidStationNameException extends RuntimeException {
    public InvalidStationNameException() {
        super("이름과 일치하는 역이 존재하지 않습니다.");
    }
}
