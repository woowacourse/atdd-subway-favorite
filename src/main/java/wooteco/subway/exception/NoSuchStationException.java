package wooteco.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class NoSuchStationException extends RuntimeException {
    public static final String NO_SUCH_STATION_EXCEPTION_MESSAGE = "해당하는 역을 찾을 수 없습니다.";

    public NoSuchStationException() {
        super(NO_SUCH_STATION_EXCEPTION_MESSAGE);
    }
}
