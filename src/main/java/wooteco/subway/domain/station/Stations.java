package wooteco.subway.domain.station;

import java.util.List;

public class Stations {
    private final List<Station> stations;

    private Stations(List<Station> stations) {
        this.stations = stations;
    }

    public static Stations from(List<Station> stations) {
        return new Stations(stations);
    }

    public Station getSourceStation(int pathId) {
        return stations.get(pathId * 2);
    }

    public Station getTargetStation(int pathId) {
        return stations.get(pathId * 2 + 1);
    }
}
