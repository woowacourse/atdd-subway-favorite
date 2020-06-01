package wooteco.subway.domain.station;

import wooteco.subway.web.exception.NoSuchValueException;

import java.util.List;

import static wooteco.subway.web.exception.NoSuchValueException.NO_SUCH_STATION_MESSAGE;

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
                .filter(it -> it.getId() == stationId)
                .findFirst()
                .orElseThrow(() -> new NoSuchValueException(NO_SUCH_STATION_MESSAGE));
    }
}
