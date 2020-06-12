package wooteco.subway.service.member.exception;

public class NotFoundMemberException extends RuntimeException {
    public static final String NOT_FOUND_MEMBER_EXCEPTION_MESSAGE = "사용자가 존재하지 않습니다.";

    public NotFoundMemberException() {
        super(NOT_FOUND_MEMBER_EXCEPTION_MESSAGE);
    }
}
