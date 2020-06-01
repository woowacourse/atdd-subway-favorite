package wooteco.subway.domain.station;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import wooteco.subway.exceptions.NotExistStationException;

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
                .filter(it -> Objects.equals(it.getId(), stationId))
                .findFirst()
                .orElseThrow(() -> new NotExistStationException(stationId));
    }

    public Map<Long, Station> convertToMap() {
        return stations.stream()
            .collect(Collectors.toMap(Station::getId, Function.identity()));
    }
}
