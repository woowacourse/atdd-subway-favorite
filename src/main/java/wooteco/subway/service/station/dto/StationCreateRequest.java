package wooteco.subway.service.station.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import wooteco.subway.domain.station.Station;

@Getter
@NoArgsConstructor
public class StationCreateRequest {
    private String name;

    public Station toStation() {
        return Station.of(name);
    }
}
