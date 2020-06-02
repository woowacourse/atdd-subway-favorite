package wooteco.subway.web.exception;

import org.springframework.dao.DataAccessException;

public class NotFoundStationException extends DataAccessException {
    public NotFoundStationException(String message) {
        super(message);
    }
}
