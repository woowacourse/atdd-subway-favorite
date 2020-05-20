package wooteco.subway.web.member;

public class InvalidRegisterException extends RuntimeException {
    public static final String INVALID_EMAIL_FORMAT_MSG = "이메일의 형식에 맞지 않습니다.";
    public static final String DUPLICATE_EMAIL_MSG = "존재하는 이메일입니다.";

    public InvalidRegisterException(String msg) {
        super(msg);
    }
}
