package woowa.bossdog.subway.service.path;

import woowa.bossdog.subway.web.advice.exception.BadRequestForResourcesException;

public class DuplicatedStationPathException extends BadRequestForResourcesException {
    private static final String ERROR_CODE = "DUPLICATED_STATION";

    public DuplicatedStationPathException() {
        super(ERROR_CODE, "출발역과 도착역은 같을 수 없습니다.");
    }
}
