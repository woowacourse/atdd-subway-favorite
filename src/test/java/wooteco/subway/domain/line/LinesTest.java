package wooteco.subway.domain.line;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LinesTest {
    @Test
    public void construct() {
        Line line = new Line("2호선", LocalTime.of(5, 30), LocalTime.of(23,30), 10);

        Lines lines = new Lines(Lists.newArrayList(line));
        assertThat(lines).isNotNull();
    }

    @Test
    public void getStationIds() {
        Line 이호선 = new Line("2호선", LocalTime.of(5, 30), LocalTime.of(23,30), 10);
        Line 신분당선 = new Line("신분당선", LocalTime.of(5, 30), LocalTime.of(23,30), 12);

        이호선.addLineStation(new LineStation(null, 1L, 10, 10));
        이호선.addLineStation(new LineStation(1L, 2L, 10, 10));
        이호선.addLineStation(new LineStation(2L, 3L, 10, 10));

        신분당선.addLineStation(new LineStation(null, 5L, 10, 10));
        신분당선.addLineStation(new LineStation(5L, 6L, 10, 10));


        Lines lines = new Lines(Lists.newArrayList(이호선, 신분당선));
        final List<Long> stationIds = lines.getStationIds();

        assertThat(stationIds).hasSize(5);
        assertThat(stationIds.get(0)).isEqualTo(1L);
        assertThat(stationIds.get(1)).isEqualTo(2L);
        assertThat(stationIds.get(2)).isEqualTo(3L);
        assertThat(stationIds.get(3)).isEqualTo(6L);
        assertThat(stationIds.get(4)).isEqualTo(5L);
    }

    @Test
    public void findLineStations() {
        Line 이호선 = new Line(1L, "2호선", LocalTime.of(5, 30), LocalTime.of(23,30), 10);

        이호선.addLineStation(new LineStation(1L, 2L, 10, 10));
        이호선.addLineStation(new LineStation(2L, 3L, 10, 10));

        final Lines lines = new Lines(Lists.newArrayList(이호선));
        final LineStations lineStations = lines.findLineStations();

        assertThat(lineStations).isNotNull();
    }


}