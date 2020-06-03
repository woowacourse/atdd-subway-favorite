package wooteco.subway.domain.line;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Lines {
    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = new ArrayList<>(Objects.requireNonNull(lines));
    }

    public List<Long> getStationIds() {
        return lines.stream()
            .flatMap(line -> line.getStations().stream())
            .map(LineStation::getStationId)
            .collect(toList());
    }

    public List<Line> getLines() {
        return lines;
    }
}
