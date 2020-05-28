package wooteco.subway.service.path;

import wooteco.subway.web.BadRequestForResourcesException;

public class NotExistedPathException extends BadRequestForResourcesException {
    private static final String ERROR_CODE = "NOT_EXISTED_PATH";

    public NotExistedPathException() {
        super(ERROR_CODE, "존재하지 않는 경로 입니다.");
    }
}
