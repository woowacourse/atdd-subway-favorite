package wooteco.subway.web.member.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotExistStationDataException extends NotExistException {
    public NotExistStationDataException(String reason) {
        super(reason);
    }
}

