package woowa.bossdog.subway.web.station;

import woowa.bossdog.subway.web.advice.exception.ResourcesNotFoundException;

public class NotExistedStationException extends ResourcesNotFoundException {
    private static final String ERROR_CODE = "NOT_EXISTED_STATION";

    public NotExistedStationException() {
        super(ERROR_CODE, "존재하지 않은 지하철 역입니다.");
    }
}
