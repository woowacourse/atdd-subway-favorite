package wooteco.subway.domain.line;

import java.util.List;
import java.util.stream.Collectors;

public class Lines {
    private List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public List<Line> getLines() {
        return lines;
    }

    public List<Long> getStationIds() {
        return lines.stream()
            .flatMap(it -> it.getStations().stream())
            .map(it -> it.getStationId())
            .collect(Collectors.toList());
    }
    //
    //    public LineStations extractLineStationByStationIds(List<Long> stationIds) {
    //        for (Long stationId : stationIds) {
    //            List<LineStation> lineStations = findLineStation(null, stationId);
    //        }
    //        Set<LineStation> lineStations = stationIds.stream()
    //                .map(it -> getLineStationByStationId(it))
    //                .collect(Collectors.toSet());
    //
    //        return new LineStations(lineStations);
    //    }
    //
    //    private LineStation getLineStationByStationId(Long stationId) {
    //        return lines.stream()
    //                .flatMap(it -> it.getStations().stream())
    ////                .filter(it -> Objects.nonNull(it.getPreStationId()))
    //                .filter(it -> it.getStationId() == stationId)
    //                .findFirst()
    //                .orElseThrow(RuntimeException::new);
    //    }
}
