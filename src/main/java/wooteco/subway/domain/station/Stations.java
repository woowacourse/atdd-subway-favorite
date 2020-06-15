package wooteco.subway.domain.station;

import java.util.List;

public class Stations {
    private List<Station> stations;

    public Stations(List<Station> stations) {
        this.stations = stations;
    }

    public static Stations of(List<Station> stations) {
        return new Stations(stations);
    }

    public List<Station> getStations() {
        return stations;
    }

    public Station extractStationById(Long stationId) {
        return stations.stream()
                .filter(it -> it.getId() == stationId)
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }
}
