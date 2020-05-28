package wooteco.subway.service.station;

import wooteco.subway.web.member.BadRequestException;

public class NoExistStationException extends BadRequestException {

    public static final String NO_EXIST_STATION_MSG = "해당하는 역이 존재하지 않습니다.";

    public NoExistStationException() {
        super(NO_EXIST_STATION_MSG);
    }
}
