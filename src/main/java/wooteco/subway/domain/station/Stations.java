package wooteco.subway.domain.station;

import java.util.List;
import java.util.Objects;

public class Stations {
    private List<Station> stations;

    public Stations(List<Station> stations) {
        this.stations = stations;
    }

    public List<Station> getStations() {
        return stations;
    }

    public Station extractStationById(Long stationId) {
        return stations.stream()
            .filter(it -> Objects.equals(it, stationId))
            .findFirst()
            .orElseThrow(RuntimeException::new);
    }
}
