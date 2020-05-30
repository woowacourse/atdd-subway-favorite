package wooteco.subway.domain.line;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.springframework.data.relational.core.mapping.MappedCollection;

import wooteco.subway.web.controlleradvice.exception.LineStationException;

public class LineStations {

    @MappedCollection(idColumn = "line", keyColumn = "index")
    private final LinkedList<LineStation> stations;

    public LineStations(List<LineStation> stations) {
        this.stations = new LinkedList<>(stations);
    }

    public static LineStations empty() {
        return new LineStations(new LinkedList<>());
    }

    public List<LineStation> getStations() {
        return stations;
    }

    public void add(LineStation lineStation) {
        validateLineStation(lineStation);
        int insertIndex = findInsertIndex(lineStation.getPreStationId());
        Long stationId = lineStation.getStationId();
        updatePreStation(insertIndex, stationId);
        stations.add(insertIndex, lineStation);
    }

    public void remove(Long stationId) {
        int index = findRemoveStationIndex(stationId);
        int indexToUpdate = index + 1;
        Long preStationdId = stations.get(index).getPreStationId();
        updatePreStation(indexToUpdate, preStationdId);
        stations.remove(index);
    }

    public List<Long> getStationIds() {
        LinkedList<Long> stationIds = new LinkedList<>();
        for (LineStation lineStation : stations) {
            stationIds.add(lineStation.getStationId());
        }
        return stationIds;
    }

    private void validateLineStation(LineStation lineStation) {
        validateAlreadyRegistered(lineStation);
        validateHavingSame(lineStation);
    }

    private void validateAlreadyRegistered(LineStation lineStation) {
        if (!lineStation.isFirstLineStation() && stations.isEmpty()) {
            throw new LineStationException("첫 노선을 먼저 등록해야 합니다.");
        }
    }

    private void validateHavingSame(LineStation lineStation) {
        for (LineStation station : stations) {
            if (station.hasSameStations(lineStation)) {
                throw new LineStationException("이미 등록된 구간입니다.");
            }
        }
    }

    private int findInsertIndex(Long preStationId) {
        if (Objects.isNull(preStationId)) {
            return 0;
        }
        LineStation last = stations.getLast();
        if (preStationId.equals(last.getStationId())) {
            return stations.size();
        }
        LineStation preStation = stations.stream()
            .filter(station -> Objects.equals(preStationId, station.getPreStationId()))
            .findAny()
            .orElseThrow(() -> new LineStationException("현재 노선에 등록되지 않은 이전역입니다."));
        return stations.indexOf(preStation);
    }

    private int findRemoveStationIndex(Long stationId) {
        LineStation lineStation = stations.stream()
            .filter(station -> Objects.equals(stationId, station.getStationId()))
            .findFirst()
            .orElseThrow(() -> new LineStationException("해당 호선에 등록되지 않은 역입니다."));
        return stations.indexOf(lineStation);
    }

    private void updatePreStation(int index, Long stationId) {
        if (stations.size() != index) {
            LineStation existing = stations.get(index);
            existing.updatePreLineStation(stationId);
        }
    }
}
