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
    public static Station station1 = new Station(1L, "일원역");
    public static Station station2 = new Station(2L, "이대역");
    public static Station station3 = new Station(3L, "삼성역");
    public static Station station4 = new Station(4L, "사당역");

    @BeforeEach
    void setUp() {
        line = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line.addLineStation(new LineStation(null, station1, 10, 10));
        line.addLineStation(new LineStation(station1, station2, 10, 10));
        line.addLineStation(new LineStation(station2, station3, 10, 10));
    }

    @Test
    void addLineStation() {
        line.addLineStation(new LineStation(null, station4, 10, 10));
        assertThat(line.getLineStations()).hasSize(4);
    }

    @Test
    void getLineStations() {
        List<Long> stationIds = line.getStationIds();

        assertThat(stationIds.size()).isEqualTo(3);
        assertThat(stationIds.get(0)).isEqualTo(1L);
        assertThat(stationIds.get(1)).isEqualTo(2L);
        assertThat(stationIds.get(2)).isEqualTo(3L);
    }
}
