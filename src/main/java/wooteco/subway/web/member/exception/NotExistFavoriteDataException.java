package wooteco.subway.web.member.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotExistFavoriteDataException extends IllegalArgumentException {
    private static final String MESSAGE_TAIL = "를 가진 데이터가 없습니다.";

    public NotExistFavoriteDataException(String data) {
        super(data + MESSAGE_TAIL);
    }
}
