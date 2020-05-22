package wooteco.subway.web.member;

public class NotFoundMemberException extends RuntimeException {

    public static final String ERROR_MESSAGE = " 를 찾을 수 없습니다.";

    public NotFoundMemberException() {
    }

    public NotFoundMemberException(String email) {
        super(email + ERROR_MESSAGE);
    }
}
