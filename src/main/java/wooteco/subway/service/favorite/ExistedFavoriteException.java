package wooteco.subway.service.favorite;

import wooteco.subway.web.BadRequestForResourcesException;

public class ExistedFavoriteException extends BadRequestForResourcesException {
    private static final String ERROR_CODE = "EXISTED_FAVORITE";

    public ExistedFavoriteException() {
        super(ERROR_CODE, "이미 추가된 즐겨찾기 구간입니다.");
    }
}
