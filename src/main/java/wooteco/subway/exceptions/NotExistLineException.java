package wooteco.subway.exceptions;

public class NotExistLineException extends RuntimeException {
    public NotExistLineException(Long id) {
        super(String.format("Id가 %d인 노선은 존재하지 않습니다!", id));
    }
}
