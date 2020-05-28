package wooteco.subway.service.path;

import wooteco.subway.web.BadRequestForResourcesException;

public class DuplicatedStationException extends BadRequestForResourcesException {
    private static final String ERROR_CODE = "DUPLICATED_STATION";

    public DuplicatedStationException() {
        super(ERROR_CODE, "출발역과 도착역은 같을 수 없습니다.");
    }
}
