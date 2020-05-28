package wooteco.subway.exception;

// Todo: advice에 등록
public class InvalidStationIdException extends RuntimeException {
    public InvalidStationIdException(String message) {
        super(message);
    }
}
