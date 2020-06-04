package wooteco.subway.domain.path;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineStation;
import wooteco.subway.domain.line.LineStations;
import wooteco.subway.domain.line.Lines;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PathCalculatorTest {
    @Test
    public void findPath() {
        final Line 이호선 = new Line("2호선", LocalTime.of(5, 30), LocalTime.of(23, 30), 10);
        final Line 신분당선 = new Line("신분당선", LocalTime.of(5, 30), LocalTime.of(23, 30), 10);

        이호선.addLineStation(new LineStation(null, 1L, 10, 10));
        이호선.addLineStation(new LineStation(1L, 7L, 10, 10));
        신분당선.addLineStation(new LineStation(null, 7L, 10, 10));
        신분당선.addLineStation(new LineStation(7L, 5L, 10, 10));
        신분당선.addLineStation(new LineStation(5L, 2L, 10, 10));

        final Lines lines = new Lines(Lists.newArrayList(이호선, 신분당선));
        final List<Long> path = PathCalculator.findPath(lines, 1L, 2L, PathType.DISTANCE);

        assertThat(path).isNotNull();
        assertThat(path.get(0)).isEqualTo(1L);
        assertThat(path.get(1)).isEqualTo(7L);
        assertThat(path.get(2)).isEqualTo(5L);
        assertThat(path.get(3)).isEqualTo(2L);
    }

    @Test
    public void extractPathLineStation() {
        final Line 이호선 = new Line("2호선", LocalTime.of(5, 30), LocalTime.of(23, 30), 10);
        final Line 신분당선 = new Line("신분당선", LocalTime.of(5, 30), LocalTime.of(23, 30), 10);

        이호선.addLineStation(new LineStation(null, 1L, 10, 10));
        이호선.addLineStation(new LineStation(1L, 7L, 10, 10));
        신분당선.addLineStation(new LineStation(null, 7L, 10, 10));
        신분당선.addLineStation(new LineStation(7L, 5L, 10, 10));
        신분당선.addLineStation(new LineStation(5L, 2L, 10, 10));

        final Lines lines = new Lines(Lists.newArrayList(이호선, 신분당선));
        final List<Long> path = PathCalculator.findPath(lines, 1L, 2L, PathType.DISTANCE);

        final LineStations lineStations = PathCalculator.extractPathLineStation(path, lines.findLineStations());

        assertThat(lineStations).isNotNull();
        assertThat(lineStations.getStations()).hasSize(3);
        assertThat(lineStations.getTotalDistance()).isEqualTo(30);
        assertThat(lineStations.getTotalDuration()).isEqualTo(30);
    }

}