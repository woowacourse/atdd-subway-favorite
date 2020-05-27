package wooteco.subway.service.station.dto;


import wooteco.subway.domain.station.Station;

import javax.validation.constraints.NotEmpty;

public class StationCreateRequest {
    @NotEmpty
    private String name;

    public StationCreateRequest() {
    }

    public StationCreateRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Station toStation() {
        return new Station(name);
    }
}
