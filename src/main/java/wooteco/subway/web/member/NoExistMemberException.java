package wooteco.subway.web.member;

public class NoExistMemberException extends BadRequestException {
    private static final String NO_EXIST_MEMBER_MSG = "해당하는 계정이 없습니다.";

    public NoExistMemberException() {
        super(NO_EXIST_MEMBER_MSG);
    }

    public NoExistMemberException(String message) {
        super(message);
    }
}
