package wooteco.subway.domain.line;

import java.util.List;
import java.util.stream.Collectors;

import wooteco.subway.domain.station.Station;

public class Lines {
    private List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public List<Long> getStationIds() {
        return lines.stream()
            .flatMap(it -> it.getLineStations().stream())
            .map(LineStation::getStation)
            .map(Station::getId)
            .collect(Collectors.toList());
    }

    public List<Line> getLines() {
        return lines;
    }
}
