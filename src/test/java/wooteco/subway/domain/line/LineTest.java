package wooteco.subway.domain.line;

import static org.assertj.core.api.Assertions.*;
import static wooteco.subway.service.line.LineServiceTest.*;

import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import wooteco.subway.domain.linestation.LineStation;
import wooteco.subway.domain.station.Station;

public class LineTest {
    private Line line;
    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;
    private LineStation lineStation1;
    private LineStation lineStation2;
    private LineStation lineStation3;

    @BeforeEach
    void setUp() {
        station1 = new Station(1L, STATION_NAME1);
        station2 = new Station(2L, STATION_NAME2);
        station3 = new Station(3L, STATION_NAME3);
        station4 = new Station(4L, STATION_NAME4);

        line = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        lineStation1 = new LineStation(null, station1, 10, 10);
        lineStation2 = new LineStation(station1, station2, 10, 10);
        lineStation3 = new LineStation(station2, station3, 10, 10);

        lineStation1.applyLine(line);
        lineStation2.applyLine(line);
        lineStation3.applyLine(line);
    }

    @Test
    void addLineStation() {
        LineStation lineStation = new LineStation(null, station4, 10, 10);
        lineStation.applyLine(line);
        assertThat(line.getStations()).hasSize(4);
    }

    @Test
    void getLineStations() {
        List<Long> stationIds = line.getStationIds();

        assertThat(stationIds.size()).isEqualTo(3);
        assertThat(stationIds.get(0)).isEqualTo(1L);
        assertThat(stationIds.get(1)).isEqualTo(2L);
        assertThat(stationIds.get(2)).isEqualTo(3L);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    void removeLineStation(Long stationId) {
        line.removeLineStationById(stationId);

        assertThat(line.getStations()).hasSize(2);
    }
}
