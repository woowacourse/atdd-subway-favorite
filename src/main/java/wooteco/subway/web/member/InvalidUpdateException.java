package wooteco.subway.web.member;

public class InvalidUpdateException extends RuntimeException {

    public InvalidUpdateException() {
        super("자신의 계정이 아닌 계정의 정보를 변경할 수 없습니다.");
    }
}
