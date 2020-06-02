package wooteco.subway.web.member.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotExistMemberDataException extends IllegalArgumentException {
    private static final String MESSAGE_TAIL = "에 해당하는 정보가 없습니다.";

    public NotExistMemberDataException(String keyword) {
        super(keyword + MESSAGE_TAIL);
    }
}
