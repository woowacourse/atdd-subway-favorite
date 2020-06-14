package wooteco.subway.service.member;

public class CreateMemberException extends RuntimeException {
    CreateMemberException(String message) {
        super(message);
    }

    CreateMemberException(Throwable cause) {
        super(cause);
    }
}
