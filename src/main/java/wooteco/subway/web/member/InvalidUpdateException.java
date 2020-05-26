package wooteco.subway.web.member;

public class InvalidUpdateException extends RuntimeException {

    public static final String INVALID_UPDATE_MSG = "자신의 계정이 아닌 계정의 정보를 변경할 수 없습니다.";

    public InvalidUpdateException() {
        super(INVALID_UPDATE_MSG);
    }
}
