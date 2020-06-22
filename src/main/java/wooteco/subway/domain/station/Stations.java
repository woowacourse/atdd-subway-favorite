package wooteco.subway.domain.station;

import wooteco.subway.service.exception.WrongStationException;

import java.util.List;

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
                .filter(it -> it.getId().equals(stationId))
                .findFirst()
                .orElseThrow(WrongStationException::new);
    }

    public Station extractStation(Long id) {
        return stations
                .stream()
                .filter(it -> it.getId().equals(id))
                .findFirst()
                .orElseThrow(WrongStationException::new);
    }
}
