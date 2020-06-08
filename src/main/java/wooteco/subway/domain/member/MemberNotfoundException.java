package wooteco.subway.domain.member;

public class MemberNotfoundException extends RuntimeException {
    public static final String INVALID_EMAIL_MESSAGE= "이메일에 해당하는 회원이 존재하지 않습니다.";
    public MemberNotfoundException(String message) {
        super(message);
    }
}
