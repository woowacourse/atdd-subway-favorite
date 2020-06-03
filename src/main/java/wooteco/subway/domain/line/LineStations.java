package wooteco.subway.domain.line;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class LineStations {
    private Set<LineStation> stations;

    public LineStations() {
    }

    public LineStations(Set<LineStation> stations) {
        this.stations = stations;
    }

    public LineStations(List<LineStation> stations) {
        this(new HashSet<>(stations));
    }

    public static LineStations empty() {
        return new LineStations(new HashSet<>());
    }

    public Set<LineStation> getStations() {
        return stations;
    }

    public void add(LineStation targetLineStation) {
        updatePreStationOfNextLineStation(targetLineStation.getPreStationId(), targetLineStation.getStationId());
        stations.add(targetLineStation);
    }

    private void remove(LineStation targetLineStation) {
        updatePreStationOfNextLineStation(targetLineStation.getStationId(), targetLineStation.getPreStationId());
        stations.remove(targetLineStation);
    }

    public void removeById(Long targetStationId) {
        extractByStationId(targetStationId)
            .ifPresent(this::remove);
    }

    public List<Long> getStationIds() {
        List<Long> result = new ArrayList<>();
        extractNext(null, result);
        return result;
    }

    private void extractNext(Long preStationId, List<Long> ids) {
        stations.stream()
            .filter(it -> it.hasSamePreStation(preStationId))
            .findFirst()
            .ifPresent(it -> {
                Long nextStationId = it.getStationId();
                ids.add(nextStationId);
                extractNext(nextStationId, ids);
            });
    }

    private void updatePreStationOfNextLineStation(Long targetStationId, Long newPreStationId) {
        extractByPreStationId(targetStationId)
            .ifPresent(it -> it.updatePreLineStation(newPreStationId));
    }

    private Optional<LineStation> extractByStationId(Long stationId) {
        return stations.stream()
            .filter(it -> it.hasSameStation(stationId))
            .findFirst();
    }

    private Optional<LineStation> extractByPreStationId(Long preStationId) {
        return stations.stream()
            .filter(it -> it.hasSamePreStation(preStationId))
            .findFirst();
    }

    public List<LineStation> getStationsExcludeFirstStation() {
        return stations.stream()
            .filter(it -> !it.isFirstOfLine())
            .collect(Collectors.toList());
    }

    public int getTotalDistance() {
        return stations.stream().mapToInt(LineStation::getDistance).sum();
    }

    public int getTotalDuration() {
        return stations.stream().mapToInt(LineStation::getDuration).sum();
    }
}
