package wooteco.subway.web.member.exception;

import java.util.NoSuchElementException;

public class NotExistFavoriteDataException extends IllegalArgumentException {
    private static final String MESSAGE_TAIL = "를 가진 데이터가 없습니다.";

    public NotExistFavoriteDataException(String data) {
        super(data + MESSAGE_TAIL);
    }
}
