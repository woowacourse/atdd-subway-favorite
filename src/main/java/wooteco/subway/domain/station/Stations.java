package wooteco.subway.domain.station;

import java.util.List;

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

    public List<Station> getStations() {
        return stations;
    }
}
