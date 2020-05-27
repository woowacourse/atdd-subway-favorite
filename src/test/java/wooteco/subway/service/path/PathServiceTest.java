package wooteco.subway.service.path;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.line.LineStation;
import wooteco.subway.domain.path.PathType;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.path.dto.PathResponse;
import wooteco.subway.service.station.dto.StationResponse;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {
    private static final String STATION_NAME1 = "강남역";
    private static final String STATION_NAME2 = "역삼역";
    private static final String STATION_NAME3 = "선릉역";
    private static final String STATION_NAME4 = "양재역";
    private static final String STATION_NAME5 = "양재시민의숲역";
    private static final String STATION_NAME6 = "서울역";

    @Mock
    private StationRepository stationRepository;
    @Mock
    private LineRepository lineRepository;
    @Mock
    private GraphService graphService;

    private PathService pathService;

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
        pathService = new PathService(stationRepository, lineRepository, graphService);

        station1 = new Station(1L, STATION_NAME1);
        station2 = new Station(2L, STATION_NAME2);
        station3 = new Station(3L, STATION_NAME3);
        station4 = new Station(4L, STATION_NAME4);
        station5 = new Station(5L, STATION_NAME5);
        station6 = new Station(6L, STATION_NAME6);

        line1 = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line1.addLineStation(new LineStation(null, 1L, 10, 10));
        line1.addLineStation(new LineStation(1L, 2L, 10, 10));
        line1.addLineStation(new LineStation(2L, 3L, 10, 10));

        line2 = new Line(2L, "신분당선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line2.addLineStation(new LineStation(null, 1L, 10, 10));
        line2.addLineStation(new LineStation(1L, 4L, 10, 10));
        line2.addLineStation(new LineStation(4L, 5L, 10, 10));
    }

    @DisplayName("일반적인 상황의 경로 찾기")
    @Test
    void findPath() {
        when(lineRepository.findAll()).thenReturn(Lists.list(line1, line2));
        when(stationRepository.findAllById(anyList())).thenReturn(
            Lists.list(station3, station2, station1, station4, station5));
        when(stationRepository.findByName(STATION_NAME3)).thenReturn(Optional.of(station3));
        when(stationRepository.findByName(STATION_NAME5)).thenReturn(Optional.of(station5));
        when(graphService.findPath(anyList(), anyLong(), anyLong(), any())).thenReturn(
            Lists.list(3L, 2L, 1L, 4L, 5L));

        PathResponse pathResponse = pathService.findPath(STATION_NAME3, STATION_NAME5,
            PathType.DISTANCE);

        List<StationResponse> paths = pathResponse.getStations();
        assertThat(paths).hasSize(5);
        assertThat(paths.get(0).getName()).isEqualTo(STATION_NAME3);
        assertThat(paths.get(1).getName()).isEqualTo(STATION_NAME2);
        assertThat(paths.get(2).getName()).isEqualTo(STATION_NAME1);
        assertThat(paths.get(3).getName()).isEqualTo(STATION_NAME4);
        assertThat(paths.get(4).getName()).isEqualTo(STATION_NAME5);
        assertThat(pathResponse.getDistance()).isEqualTo(40);
        assertThat(pathResponse.getDuration()).isEqualTo(40);
    }

    @DisplayName("출발역과 도착역이 같은 경우")
    @Test
    void findPathWithSameSourceAndTarget() {
        assertThrows(RuntimeException.class,
            () -> pathService.findPath(STATION_NAME3, STATION_NAME3, PathType.DISTANCE));
    }

    @DisplayName("출발역과 도착역이 연결이 되지 않은 경우")
    @Test
    void findPathWithNoPath() {
        assertThrows(RuntimeException.class,
            () -> pathService.findPath(STATION_NAME3, STATION_NAME6, PathType.DISTANCE));
    }
}
