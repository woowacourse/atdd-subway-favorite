package wooteco.subway.web.exceptions;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException() {
        super("회원정보를 찾을 수 없습니다.");
    }
}
