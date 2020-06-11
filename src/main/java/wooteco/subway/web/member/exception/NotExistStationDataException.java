package wooteco.subway.web.member.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotExistStationDataException extends IllegalArgumentException {
    private static final String MESSAGE_TAIL = "를(을) 가진 데이터가 없습니다.";

    public NotExistStationDataException(String reason) {
        super(reason + MESSAGE_TAIL);
    }
}

