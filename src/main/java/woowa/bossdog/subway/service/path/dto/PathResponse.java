package woowa.bossdog.subway.service.path.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import woowa.bossdog.subway.service.station.dto.StationResponse;

import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int duration;

    public PathResponse(final List<StationResponse> stations, final int distance, final int duration) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
    }
}
