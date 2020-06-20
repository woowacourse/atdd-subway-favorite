package wooteco.subway.domain.line;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import wooteco.subway.domain.station.Station;

@Embeddable
public class LineStations {
    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "line_id")
    private Set<LineStation> stations;

    protected LineStations() {
    }

    public LineStations(Set<LineStation> stations) {
        this.stations = stations;
    }

    public static LineStations empty() {
        return new LineStations(new HashSet<>());
    }

    public void add(LineStation newLineStation) {
        List<Station> stationsInOrder = getStationsInOrder();
        if (stations.size() == 0) {
            if (newLineStation.getPreStation() == null) {
                stations.add(newLineStation);
                return;
            }
            stations.add(newLineStation);
            stations.add(new LineStation(null, newLineStation.getPreStation(), 0, 0));
        }
        if (stationsInOrder.get(stations.size() - 1).equals(newLineStation.getPreStation())) {
            stations.add(newLineStation);
            return;
        }
        if (newLineStation.getPreStation() == null) {
            stations.stream()
                .filter(lineStation -> lineStation.getPreStation() == null)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("출발역이 존재하지 않습니다."))
                .updatePreLineStation(newLineStation.getStation());
            stations.add(newLineStation);
            return;
        }
        stations.stream()
            .filter(
                lineStation -> newLineStation.getPreStation().equals(lineStation.getPreStation()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("해당하는 구간이 없습니다."))
            .updatePreLineStation(newLineStation.getStation());
        stations.add(newLineStation);
    }

    public void remove(LineStation targetLineStation) {
        updatePreStationOfNextLineStation(targetLineStation.getStation().getId(),
            targetLineStation.getPreStation());
        stations.remove(targetLineStation);
    }

    public void removeById(Long targetStationId) {
        extractByStationId(targetStationId)
            .ifPresent(this::remove);
    }

    public List<Long> getStationIds() {
        return getStationsInOrder().stream().map(Station::getId).collect(Collectors.toList());
    }

    public List<Station> getStationsInOrder() {
        List<Station> result = new ArrayList<>();
        extractNext(null, result);
        return result;
    }

    private void extractNext(Station preStation, List<Station> stations) {
        for (int i = 0; i < this.stations.size(); i++) {
            final Station finalPreStation = preStation;
            this.stations.stream()
                .filter(station -> Objects.equals(station.getPreStation(), finalPreStation))
                .findFirst()
                .ifPresent(station -> stations.add(station.getStation()));
            preStation = stations.get(stations.size() - 1);
        }
    }

    private void updatePreStationOfNextLineStation(Long targetStationId, Station newPreStation) {
        extractByPreStationId(targetStationId)
            .ifPresent(it -> it.updatePreLineStation(newPreStation));
    }

    private Optional<LineStation> extractByStationId(Long stationId) {
        return stations.stream()
            .filter(it -> Objects.nonNull(it.getStation()))
            .filter(it -> Objects.equals(it.getStation().getId(), stationId))
            .findFirst();
    }

    private Optional<LineStation> extractByPreStationId(Long preStationId) {
        return stations.stream()
            .filter(it -> Objects.nonNull(it.getPreStation()))
            .filter(it -> Objects.equals(it.getPreStation().getId(), preStationId))
            .findFirst();
    }

    public Set<LineStation> getStations() {
        return stations;
    }
}
