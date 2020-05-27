package wooteco.subway.service.line.dto;

import javax.validation.constraints.NotNull;

public class LineStationCreateRequest {
    @NotNull
    private Long preStationId;
    @NotNull
    private Long stationId;
    @NotNull
    private int distance;
    @NotNull
    private int duration;

    private LineStationCreateRequest() {
    }

    public LineStationCreateRequest(Long preStationId, Long stationId, int distance, int duration) {
        this.preStationId = preStationId;
        this.stationId = stationId;
        this.distance = distance;
        this.duration = duration;
    }

    public Long getPreStationId() {
        return preStationId;
    }

    public Long getStationId() {
        return stationId;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }
}
