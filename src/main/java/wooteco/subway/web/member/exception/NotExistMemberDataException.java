package wooteco.subway.web.member.exception;

public class NotExistMemberDataException extends NotExistDataException {
    private static final String MESSAGE_TAIL = "에 해당하는 정보가 없습니다.";

    public NotExistMemberDataException(String keyword) {
        super(keyword + MESSAGE_TAIL);
    }
}
