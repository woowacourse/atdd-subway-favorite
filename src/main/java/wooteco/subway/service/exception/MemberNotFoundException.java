package wooteco.subway.service.exception;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException() {
        this("MEMBER_NOT_FOUND");
    }

    public MemberNotFoundException(String message) {
        super(message);
    }
}
