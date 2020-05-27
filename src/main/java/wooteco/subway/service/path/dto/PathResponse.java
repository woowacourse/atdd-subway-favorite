package wooteco.subway.service.path.dto;

import java.util.List;

import wooteco.subway.service.station.dto.StationResponse;

public class PathResponse {
    private List<StationResponse> stations;
    private Integer duration;
    private Integer distance;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, Integer duration, Integer distance) {
        this.stations = stations;
        this.duration = duration;
        this.distance = distance;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDuration() {
        return duration;
    }

    public int getDistance() {
        return distance;
    }
}
