package wooteco.subway.domain.linestation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import wooteco.subway.domain.station.Station;

@Embeddable
@NoArgsConstructor
@Getter
public class LineStations {

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LineStation> stations;

    public LineStations(List<LineStation> stations) {
        this.stations = stations;
    }

    public static LineStations empty() {
        return new LineStations(new ArrayList<>());
    }

    public List<LineStation> getStations() {
        return stations;
    }

    public void add(LineStation targetLineStation) {
        updatePreStationOfNextLineStation(targetLineStation.getPreStation().getId(),
            targetLineStation.getNextStation());
        stations.add(targetLineStation);
    }

    private void remove(LineStation targetLineStation) {
        updatePreStationOfNextLineStation(targetLineStation.getNextStation().getId(),
            targetLineStation.getPreStation());
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
            .filter(it -> Objects.equals(it.getPreStation().getId(), preStationId))
            .findFirst()
            .ifPresent(it -> {
                Long nextStationId = it.getNextStation().getId();
                ids.add(nextStationId);
                extractNext(nextStationId, ids);
            });
    }

    private void updatePreStationOfNextLineStation(Long targetStationId, Station newPreStation) {
        extractByPreStationId(targetStationId)
            .ifPresent(it -> it.updatePreLineStation(newPreStation));
    }

    private Optional<LineStation> extractByStationId(Long stationId) {
        return stations.stream()
            .filter(it -> Objects.equals(it.getNextStation().getId(), stationId))
            .findFirst();
    }

    private Optional<LineStation> extractByPreStationId(Long preStationId) {
        return stations.stream()
            .filter(it -> Objects.equals(it.getPreStation().getId(), preStationId))
            .findFirst();
    }
}
