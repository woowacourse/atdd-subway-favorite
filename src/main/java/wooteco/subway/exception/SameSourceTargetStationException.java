package wooteco.subway.exception;

public class SameSourceTargetStationException extends RuntimeException {
    private static final String EXCEPTION_MESSAGE = "경로 등록시 시작역과 도착역이 같을 수 없습니다.";

    public SameSourceTargetStationException() {
        super(EXCEPTION_MESSAGE);
    }
}
