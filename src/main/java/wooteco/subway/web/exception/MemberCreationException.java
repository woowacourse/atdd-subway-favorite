package wooteco.subway.web.exception;

public class MemberCreationException extends SubwayException {
    public static final String DUPLICATED_EMAIL = "이미 가입된 이메일 주소입니다.";

    public MemberCreationException(String message) {
        super(message);
    }
}
