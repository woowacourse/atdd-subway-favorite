package wooteco.subway.service.member;

public class CreateMemberException extends RuntimeException {
    public static final String WRONG_CREATE_MESSAGE = "잘못된 회원가입 시도입니다.";

    CreateMemberException(String message) {
        super(message);
    }
}
