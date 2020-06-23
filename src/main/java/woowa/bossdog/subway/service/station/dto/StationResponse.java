package woowa.bossdog.subway.service.station.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import woowa.bossdog.subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StationResponse {
    private Long id;
    private String name;

    private StationResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static StationResponse from(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    public static List<StationResponse> listFrom(final List<Station> stations) {
        return stations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }
}
