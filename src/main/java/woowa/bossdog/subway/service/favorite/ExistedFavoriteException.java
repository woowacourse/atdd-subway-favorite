package woowa.bossdog.subway.service.favorite;

import woowa.bossdog.subway.web.advice.exception.BadRequestForResourcesException;

public class ExistedFavoriteException extends BadRequestForResourcesException {
    private static final String ERROR_CODE = "EXISTED_FAVORITE";

    public ExistedFavoriteException() {
        super(ERROR_CODE, "이미 등록된 즐겨찾기 경로입니다.");
    }
}
