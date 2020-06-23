package woowa.bossdog.subway.web.path;

import woowa.bossdog.subway.web.advice.exception.BadRequestForResourcesException;

public class NotExistedPathException extends BadRequestForResourcesException {
    private static final String ERROR_CODE = "NOT_EXISTED_PATH";

    public NotExistedPathException() {
        super(ERROR_CODE, "경로가 존재하지 않습니다.");
    }
}
