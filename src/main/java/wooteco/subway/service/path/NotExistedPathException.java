package wooteco.subway.service.path;

public class NotExistedPathException extends RuntimeException {
    public NotExistedPathException() {
        super("존재하지 않는 경로 입니다.");
    }
}
