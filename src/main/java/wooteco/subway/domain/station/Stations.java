package wooteco.subway.domain.station;

import java.util.List;

public class Stations {
    private List<Station> stations;

    public Stations(List<Station> stations) {
        this.stations = stations;
    }

    public List<Station> getStations() {
        return stations;
    }

    public String getNameByIndex(int index) {
        return stations.get(index).getName();
    }
}
