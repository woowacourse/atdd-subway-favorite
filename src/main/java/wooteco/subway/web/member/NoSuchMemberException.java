package wooteco.subway.web.member;

public class NoSuchMemberException extends RuntimeException {

    public static final String NO_SUCH_MEMBER_EXCEPTION_MESSAGE = "해당하는 멤버를 찾을 수 없습니다.";

    public NoSuchMemberException() {
        super(NO_SUCH_MEMBER_EXCEPTION_MESSAGE);
    }
}
