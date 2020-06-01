package wooteco.subway.domain.line;

import wooteco.subway.exception.NoLineStationExistsException;

import java.util.*;

public class LineStations {
    private final Set<LineStation> stations;

    public LineStations(Set<LineStation> stations) {
        this.stations = stations;
    }

    public static LineStations of(List<LineStation> lines) {
        return new LineStations(new HashSet<>(lines));
    }

    public static LineStations empty() {
        return new LineStations(new HashSet<>());
    }

    public LineStation findLineStation(Long preStationId, Long stationId) {
        return stations.stream()
                .filter(lineStation -> lineStation.isLineStationOf(preStationId, stationId))
                .findFirst()
                .orElseThrow(NoLineStationExistsException::new);
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


    public int extractShortestDistance() {
        return stations.stream()
                .mapToInt(LineStation::getDuration)
                .sum();
    }

    public int extractShortestDuration() {
        return stations.stream()
                .mapToInt(LineStation::getDuration)
                .sum();
    }

    public int getTotalDistance() {
        return stations.stream().mapToInt(it -> it.getDistance()).sum();
    }

    public int getTotalDuration() {
        return stations.stream().mapToInt(it -> it.getDuration()).sum();
    }
}
