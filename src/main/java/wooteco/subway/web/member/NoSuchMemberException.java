package wooteco.subway.web.member;

public class NoSuchMemberException extends RuntimeException {
    private static final String NO_SUCH_MEMBER_MSG = "해당하는 계정이 없습니다.";

    public NoSuchMemberException() {
        super(NO_SUCH_MEMBER_MSG);
    }
}
