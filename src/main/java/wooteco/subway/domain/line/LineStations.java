package wooteco.subway.domain.line;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import wooteco.subway.domain.station.Station;

@Embeddable
public class LineStations {
    @OneToMany(mappedBy = "line")
    private Set<LineStation> lineStations;

    protected LineStations() {
    }

    public LineStations(Set<LineStation> lineStations) {
        this.lineStations = lineStations;
    }

    public static LineStations empty() {
        return new LineStations(new HashSet<>());
    }

    public void add(LineStation targetLineStation) {
        updatePreStationOfNextLineStation(targetLineStation.getPreStation(),
            targetLineStation.getStation());
        lineStations.add(targetLineStation);
    }

    private void remove(LineStation targetLineStation) {
        updatePreStationOfNextLineStation(targetLineStation.getStation(),
            targetLineStation.getPreStation());
        lineStations.remove(targetLineStation);
    }

    public void removeById(Long targetStationId) {
        extractByStationId(targetStationId)
            .ifPresent(this::remove);
    }

    public Set<LineStation> getValues() {
        return lineStations;
    }

    public List<Long> getLineStationIds() {
        List<Long> result = new ArrayList<>();
        extractNextId(null, result);
        return result;
    }

    private void extractNextId(Station preStation, List<Long> ids) {
        lineStations.stream()
            .filter(it -> Objects.equals(it.getPreStation(), preStation))
            .findFirst()
            .ifPresent(it -> {
                Station nextStation = it.getStation();
                ids.add(nextStation.getId());
                extractNextId(nextStation, ids);
            });
    }

    public List<Station> getSortedStations() {
        List<Station> result = new ArrayList<>();
        extractNext(null, result);
        return result;
    }

    private void extractNext(Station preStation, List<Station> stations) {
        lineStations.stream()
            .filter(it -> Objects.equals(it.getPreStation(), preStation))
            .findFirst()
            .ifPresent(it -> {
                Station nextStation = it.getStation();
                stations.add(nextStation);
                extractNext(nextStation, stations);
            });
    }

    private void updatePreStationOfNextLineStation(Station targetStation, Station newPreStation) {
        extractByPreStationId(targetStation)
            .ifPresent(it -> it.updatePreLineStation(newPreStation));
    }

    private Optional<LineStation> extractByStationId(Long stationId) {
        return lineStations.stream()
            .filter(it -> Objects.equals(it.getStation().getId(), stationId))
            .findFirst();
    }

    private Optional<LineStation> extractByPreStationId(Station preStation) {
        return lineStations.stream()
            .filter(it -> Objects.equals(it.getPreStation(), preStation))
            .findFirst();
    }

    public int getTotalDistance() {
        return lineStations.stream().mapToInt(LineStation::getDistance).sum();
    }

    public int getTotalDuration() {
        return lineStations.stream().mapToInt(LineStation::getDuration).sum();
    }
}
