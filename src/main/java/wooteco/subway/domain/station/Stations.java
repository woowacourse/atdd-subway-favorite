package wooteco.subway.domain.station;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Stations {
    private List<Station> stations;

    public Stations(List<Station> stations) {
        this.stations = stations;
    }

    public Station extractStationById(Long stationId) {
        return stations.stream()
            .filter(it -> it.getId().equals(stationId))
            .findFirst()
            .orElseThrow(RuntimeException::new);
    }

    public Map<Long, String> toMap() {
        return stations.stream()
            .collect(Collectors.toMap(Station::getId, Station::getName));
    }

    public List<Station> getStations() {
        return stations;
    }
}
