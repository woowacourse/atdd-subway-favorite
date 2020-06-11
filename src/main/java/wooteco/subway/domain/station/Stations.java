package wooteco.subway.domain.station;

import java.util.List;
import java.util.stream.Collectors;

public class Stations {
    private List<Station> stations;

    public Stations(List<Station> stations) {
        this.stations = stations;
    }

    public List<Station> getStations() {
        return stations;
    }

    public List<Long> getStationIds() {
        return stations.stream()
                .map(Station::getId)
                .collect(Collectors.toList());
    }
}
