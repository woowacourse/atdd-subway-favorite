package woowa.bossdog.subway.web.path;

import woowa.bossdog.subway.web.advice.exception.BadRequestForResourcesException;

public class DuplicatedStationException extends BadRequestForResourcesException {
    private static final String ERROR_CODE = "DUPLICATED_STATION";

    public DuplicatedStationException() {
        super(ERROR_CODE, "지하철역 이름이 중복되었습니다.");
    }
}
