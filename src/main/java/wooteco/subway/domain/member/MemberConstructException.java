package wooteco.subway.domain.member;

public class MemberConstructException extends RuntimeException {

    public static final String EMPTY_NAME_MESSAGE = "이름이 비어있을 수 없습니다.";
    static final String EMPTY_PASSWORD_MESSAGE = "패스워드가 비어있을 수 없습니다.";
    static final String EMPTY_EMAIL_MESSAGE = "이메일이 비어있을 수 없습니다.";
    static final String INVALID_EMAIL_FORM_MESSAGE = "이메일 형식이 잘못됐습니다.";

    MemberConstructException(String message) {
        super(message);
    }
}
