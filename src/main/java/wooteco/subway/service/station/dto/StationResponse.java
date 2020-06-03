package wooteco.subway.service.station.dto;

import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.Stations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class StationResponse {
    private Long id;
    private String name;
    private LocalDateTime createdAt;

    private StationResponse() {
    }

    public StationResponse(Long id, String name, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    public static StationResponse from(Station station) {
        return new StationResponse(station.getId(), station.getName(), station.getCreatedAt());
    }

    public static List<StationResponse> listFrom(List<Station> stations) {
        return stations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }

    public static List<StationResponse> listFrom(Stations stations) {
        return listFrom(stations.getStations());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
