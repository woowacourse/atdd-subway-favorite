package wooteco.subway.domain.line;

import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.Stations;

import java.util.*;
import java.util.stream.Collectors;

public class LineStations {
    private Set<LineStation> stations;

    private LineStations() {
    }

    public LineStations(Set<LineStation> stations) {
        this.stations = stations;
    }

    public LineStations(final List<LineStation> stations) {
        this.stations = new HashSet<>(stations);
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

    public void removeById(Long targetStationId) {
        extractByStationId(targetStationId)
                .ifPresent(this::remove);
    }

    private void remove(LineStation targetLineStation) {
        updatePreStationOfNextLineStation(targetLineStation.getStationId(), targetLineStation.getPreStationId());
        stations.remove(targetLineStation);
    }

    public List<Long> getStationIds() {
        List<Long> result = new ArrayList<>();
        extractNext(null, result);
        return result;
    }

    private void extractNext(Long preStationId, List<Long> ids) {
        stations.stream()
                .filter(it -> Objects.equals(it.getPreStationId(), preStationId))
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
                .filter(it -> Objects.equals(it.getStationId(), stationId))
                .findFirst();
    }

    private Optional<LineStation> extractByPreStationId(Long preStationId) {
        return stations.stream()
                .filter(it -> Objects.equals(it.getPreStationId(), preStationId))
                .findFirst();
    }

    public int getTotalDistance() {
        return stations.stream().mapToInt(LineStation::getDistance).sum();
    }

    public int getTotalDuration() {
        return stations.stream().mapToInt(LineStation::getDuration).sum();
    }

    public LineStation findLineStation(final Long stationId, final Long finalPreStationId) {
        return stations.stream()
                .filter(it -> it.isLineStationOf(finalPreStationId, stationId))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public Stations findStations(final List<Long> path, final List<Station> stations) {
        return new Stations(path.stream()
                .map(it -> extractStation(it, stations))
                .collect(Collectors.toList()));
    }

    private Station extractStation(Long stationId, List<Station> stations) {
        return stations.stream()
                .filter(it -> it.getId().equals(stationId))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }
}
