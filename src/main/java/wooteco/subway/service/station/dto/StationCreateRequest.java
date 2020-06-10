package wooteco.subway.service.station.dto;


import wooteco.subway.domain.station.Station;

import javax.validation.constraints.NotEmpty;

public class StationCreateRequest {
    @NotEmpty(message = "이름을 입력해주세요.")
    private String name;

    public String getName() {
        return name;
    }

    public Station toStation() {
        return new Station(name);
    }
}
