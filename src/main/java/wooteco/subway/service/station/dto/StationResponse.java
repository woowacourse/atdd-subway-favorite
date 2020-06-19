package wooteco.subway.service.station.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wooteco.subway.domain.station.Station;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class StationResponse {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static StationResponse of(Station station) {
        return new StationResponse(station.getId(), station.getName(), station.getCreatedAt(),
            station.getUpdatedAt());
    }

    public static List<StationResponse> listOf(List<Station> stations) {
        return stations.stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
    }
}
