package wooteco.subway.web.member;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateMemberException extends RuntimeException {

    public static final String DUPLICATION_MESSAGE = "중복되는 이메일이 있습니다.";

    public DuplicateMemberException() {
        super(DUPLICATION_MESSAGE);
    }
}
