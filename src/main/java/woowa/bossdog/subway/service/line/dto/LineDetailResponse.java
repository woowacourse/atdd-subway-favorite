package woowa.bossdog.subway.service.line.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import woowa.bossdog.subway.domain.Line;
import woowa.bossdog.subway.domain.Station;
import woowa.bossdog.subway.service.station.dto.StationResponse;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LineDetailResponse {

    private Long id;
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private int intervalTime;

    private List<StationResponse> stations = new ArrayList<>();

    private LineDetailResponse(final Long id, final String name, final LocalTime startTime, final LocalTime endTime, final int intervalTime, final List<Station> stations) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalTime = intervalTime;
        this.stations = StationResponse.listFrom(stations);
    }

    public static LineDetailResponse of(final Line line, List<Station> stations) {
        return new LineDetailResponse(line.getId(), line.getName(), line.getStartTime(), line.getEndTime(), line.getIntervalTime(), stations);
    }
}
