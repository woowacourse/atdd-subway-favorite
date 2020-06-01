package wooteco.subway.exception;

public class MemberNotFoundException extends EntityNotFoundException {
    private static final String EXCEPTION_MESSAGE = "해당하는 회원이 존재하지 않습니다.";

    public MemberNotFoundException() {
        super(EXCEPTION_MESSAGE);
    }
}
