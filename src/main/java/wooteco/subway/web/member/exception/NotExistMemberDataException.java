package wooteco.subway.web.member.exception;

public class NotExistMemberDataException extends NotExistException {
    public NotExistMemberDataException(String email) {
        super(email);
    }
}
