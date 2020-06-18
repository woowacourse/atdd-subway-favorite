package wooteco.subway.domain.line;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class LineStations {
    @OneToMany
    private Set<LineStation> stations;

    protected LineStations() {
    }

    public LineStations(Set<LineStation> stations) {
        this.stations = stations;
    }

    public static LineStations empty() {
        return new LineStations(new HashSet<>());
    }

    public void add(LineStation targetLineStation) {
        updatePreStationOfNextLineStation(targetLineStation.getPreStationId(),
            targetLineStation.getStationId());
        stations.add(targetLineStation);
    }

    private void remove(LineStation targetLineStation) {
        updatePreStationOfNextLineStation(targetLineStation.getStationId(),
            targetLineStation.getPreStationId());
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
        for (int i = 0; i < stations.size(); i++) {
            final Long finalPreStationId = preStationId;
            stations.stream()
                .filter(station -> Objects.equals(station.getPreStationId(), finalPreStationId))
                .findFirst()
                .ifPresent(station -> {
                    ids.add(station.getStationId());
                });
            preStationId = ids.get(ids.size() - 1);
        }
    }

    private void updatePreStationOfNextLineStation(Long targetStationId, Long newPreStationId) {
        extractByPreStationId(targetStationId)
            .ifPresent(it -> it.updatePreLineStation(newPreStationId));
    }

    private Optional<LineStation> extractByStationId(Long stationId) {
        return stations.stream()
            .filter(it -> Objects.equals(it.getStationId(), stationId))
            .findFirst();
    }

    private Optional<LineStation> extractByPreStationId(Long preStationId) {
        return stations.stream()
            .filter(it -> Objects.equals(it.getPreStationId(), preStationId))
            .findFirst();
    }

    public Set<LineStation> getStations() {
        return stations;
    }
}
