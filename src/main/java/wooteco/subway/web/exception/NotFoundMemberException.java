package wooteco.subway.web.exception;

import org.springframework.dao.DataAccessException;

public class NotFoundMemberException extends DataAccessException {

    public static final String ERROR_MESSAGE = " 회원님을 찾을 수 없습니다.";

    public NotFoundMemberException(String email) {
        super(email + ERROR_MESSAGE);
    }
}
