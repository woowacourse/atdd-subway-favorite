package wooteco.subway.domain.line;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LineStationsTest {

    private LineStations lineStations;

    @BeforeEach
    void setUp() {
        lineStations = LineStations.empty();

        LineStation lineStation1 = new LineStation(null, 1L, 10, 10);
        LineStation lineStation2 = new LineStation(1L, 2L, 10, 10);
        LineStation lineStation3 = new LineStation(2L, 3L, 10, 10);

        lineStations.add(lineStation1);
        lineStations.add(lineStation2);
        lineStations.add(lineStation3);
    }

    @Test
    void add() {
        LineStation lineStation4 = new LineStation(3L, 4L, 10, 10);
        lineStations.add(lineStation4);

        assertThat(lineStations.getStations()).hasSize(4);
    }

    @Test
    void remove() {
        lineStations.removeById(3L);
        assertThat(lineStations.getStations()).hasSize(2);
    }

    @Test
    void getStationIds() {
        List<Long> ids = lineStations.getStationIds();
        assertThat(ids).hasSize(3);
        assertThat(ids).contains(1L);
        assertThat(ids).contains(2L);
        assertThat(ids).contains(3L);
    }
}