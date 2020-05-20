package wooteco.subway.service.member.exception;

public class DuplicateMemberException extends RuntimeException {
    private static final String DUPLICATE_MEMBER_EXCEPTION_FORMAT = "%s 이메일을 가진 회원이 존재합니다.";

    public DuplicateMemberException(String email) {
        super(String.format(DUPLICATE_MEMBER_EXCEPTION_FORMAT, email));
    }
}
