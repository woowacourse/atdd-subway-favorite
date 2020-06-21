package woowa.bossdog.subway.service.line.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LineStationRequest {

    private Long preStationId;
    private Long stationId;
    private int distance;
    private int duration;

    public LineStationRequest(final Long preStationId, final Long stationId, final int distance, final int duration) {
        this.preStationId = preStationId;
        this.stationId = stationId;
        this.distance = distance;
        this.duration = duration;
    }
}
