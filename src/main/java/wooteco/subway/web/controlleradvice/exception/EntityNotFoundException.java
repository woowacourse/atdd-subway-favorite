package wooteco.subway.web.controlleradvice.exception;

public class EntityNotFoundException extends IllegalArgumentException {
    public EntityNotFoundException(String entity, Long id) {
        super(String.format("%s의 %d 아이디를 찾을 수 없습니다.", entity, id));
    }
}
