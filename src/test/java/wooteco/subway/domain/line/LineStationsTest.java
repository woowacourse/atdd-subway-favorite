package wooteco.subway.domain.line;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LineStationsTest {

    private LineStations lineStations;

    @BeforeEach
    public void setUp() {
        lineStations = new LineStations(
                Lists.newArrayList(
                        new LineStation(null, 1L, 10, 10),
                        new LineStation(1L, 2L, 10, 10),
                        new LineStation(2L, 3L, 10, 10),
                        new LineStation(3L, 4L, 10, 10)
                )
        );
    }

    @Test
    public void add() {
        int beforeSize = lineStations.getStations().size();
        lineStations.add(new LineStation(6L, 3L, 10, 10));

        assertThat(lineStations.getStations().size() - beforeSize).isEqualTo(1);
    }

    @Test
    void removeById() {
        int beforeSize = lineStations.getStations().size();

        lineStations.removeById(2L);
        assertThat(lineStations.getStations().size() - beforeSize).isEqualTo(-1);
    }

    @Test
    public void getStationIds() {
        final List<Long> stationIds = lineStations.getStationIds();

        assertThat(stationIds.get(0)).isEqualTo(1L);
        assertThat(stationIds.get(1)).isEqualTo(2L);
        assertThat(stationIds.get(2)).isEqualTo(3L);
        assertThat(stationIds.get(3)).isEqualTo(4L);
    }


    @Test
    public void getTotalDistance() {

    }

    @Test
    public void getTotalDuration() {

    }

    @Test
    void findLineStation() {

    }

    @Test
    void findStations() {

    }
}