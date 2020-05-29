package wooteco.subway.service.path;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineStation;
import wooteco.subway.domain.path.PathType;
import wooteco.subway.domain.station.Station;
import wooteco.subway.service.path.dto.PathResponse;
import wooteco.subway.service.station.dto.StationResponse;

public class GraphsTest {
    private static final String STATION_NAME1 = "강남역";
    private static final String STATION_NAME2 = "역삼역";
    private static final String STATION_NAME3 = "선릉역";
    private static final String STATION_NAME4 = "양재역";
    private static final String STATION_NAME5 = "양재시민의숲역";
    private static final String STATION_NAME6 = "서울역";

    private Graphs graphs;

    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;
    private Station station5;
    private Station station6;

    private Line line1;
    private Line line2;

    @BeforeEach
    void setUp() {
        graphs = new Graphs();

        station1 = new Station(1L, STATION_NAME1);
        station2 = new Station(2L, STATION_NAME2);
        station3 = new Station(3L, STATION_NAME3);
        station4 = new Station(4L, STATION_NAME4);
        station5 = new Station(5L, STATION_NAME5);
        station6 = new Station(6L, STATION_NAME6);

        line1 = Line.of(1L, "2호선", "green", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line1.addLineStation(LineStation.of(null, 1L, 10, 10));
        line1.addLineStation(LineStation.of(1L, 2L, 10, 10));
        line1.addLineStation(LineStation.of(2L, 3L, 10, 10));

        line2 = Line.of(2L, "신분당선", "deep-red", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line2.addLineStation(LineStation.of(null, 1L, 10, 10));
        line2.addLineStation(LineStation.of(1L, 4L, 10, 10));
        line2.addLineStation(LineStation.of(4L, 5L, 10, 10));

        graphs.initialize(Lists.list(line1, line2),
            Lists.list(station1, station2, station3, station4, station5, station6));
    }

    @Test
    void findPath() {
        PathResponse path = graphs.findPath(station3.getId(), station5.getId(), PathType.DISTANCE);

        List<Long> stationIds = path.getStations().stream()
            .map(StationResponse::getId)
            .collect(Collectors.toList());
        assertThat(stationIds).hasSize(5);
        assertThat(stationIds.get(0)).isEqualTo(3L);
        assertThat(stationIds.get(1)).isEqualTo(2L);
        assertThat(stationIds.get(2)).isEqualTo(1L);
        assertThat(stationIds.get(3)).isEqualTo(4L);
        assertThat(stationIds.get(4)).isEqualTo(5L);
    }

    @Test
    void findPathWithNoPath() {
        assertThrows(IllegalArgumentException.class, () ->
            graphs.findPath(station3.getId(), station6.getId(), PathType.DISTANCE)
        );

    }

    @Test
    void findPathWithDisconnected() {
        line2.removeLineStationById(station1.getId());

        assertThrows(IllegalArgumentException.class,
            () -> graphs.findPath(station3.getId(), station6.getId(), PathType.DISTANCE)
        );
    }
}
