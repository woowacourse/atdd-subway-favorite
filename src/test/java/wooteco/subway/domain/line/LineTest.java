package wooteco.subway.domain.line;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import wooteco.subway.domain.station.Station;

public class LineTest {
    private Line line;
    private Station station1;
    private Station station2;
    private Station station3;

    @BeforeEach
    void setUp() {
        line = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        station1 = new Station(1L, "잠실역");
        station2 = new Station(2L, "선릉역");
        station3 = new Station(3L, "강변역");
        line.addLineStation(new LineStation(1L, line, null, station1, 10, 10));
        line.addLineStation(new LineStation(2L, line, station1, station2, 10, 10));
        line.addLineStation(new LineStation(3L, line, station2, station3, 10, 10));
    }

    @Test
    void addLineStation() {
        line.addLineStation(new LineStation(4L, line, null, new Station(4L, "사당역"), 10, 10));
        assertThat(line.getLineStations().getValues()).hasSize(4);
    }

    @Test
    void getLineStations() {
        List<Long> stationIds = line.getLineStationIds();

        assertThat(stationIds.size()).isEqualTo(3);
        assertThat(stationIds.get(0)).isEqualTo(1L);
        assertThat(stationIds.get(1)).isEqualTo(2L);
        assertThat(stationIds.get(2)).isEqualTo(3L);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    void removeLineStation(Long stationId) {
        line.removeLineStationById(stationId);

        assertThat(line.getLineStations().getValues()).hasSize(2);
    }
}
