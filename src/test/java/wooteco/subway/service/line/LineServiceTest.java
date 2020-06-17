package wooteco.subway.service.line;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.linestation.LineStation;
import wooteco.subway.domain.station.Station;
import wooteco.subway.service.line.dto.LineStationCreateRequest;

@ExtendWith(MockitoExtension.class)
public class LineServiceTest {
    public static final String STATION_NAME1 = "강남역";
    public static final String STATION_NAME2 = "역삼역";
    public static final String STATION_NAME3 = "선릉역";
    public static final String STATION_NAME4 = "삼성역";

    @Mock
    private LineRepository lineRepository;
    @Mock
    private LineStationService lineStationService;

    private LineService lineService;

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
        lineService = new LineService(lineStationService, lineRepository);

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
    void addLineStationAtTheFirstOfLine() {
        when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));
        when(lineStationService.findAllByStation(anyList())).thenReturn(
            Arrays.asList(null, station4));
        LineStationCreateRequest request = new LineStationCreateRequest(null, station4.getId(), 10,
            10);
        lineService.addLineStation(line.getId(), request);

        assertThat(line.getStations()).hasSize(4);

        List<Long> stationIds = line.getStationIds();
        assertThat(stationIds).containsExactlyInAnyOrder(1L, 2L, 3L, 4L);
    }

    @Test
    void addLineStationBetweenTwo() {
        when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));
        when(lineStationService.findAllByStation(anyList())).thenReturn(
            Arrays.asList(station1, station4));

        LineStationCreateRequest request = new LineStationCreateRequest(station1.getId(),
            station4.getId(), 10, 10);
        lineService.addLineStation(line.getId(), request);

        assertThat(line.getStations()).hasSize(4);

        List<Long> stationIds = line.getStationIds();
        assertThat(stationIds).containsExactlyInAnyOrder(1L, 2L, 3L, 4L);
    }

    @Test
    void addLineStationAtTheEndOfLine() {
        when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));
        when(lineStationService.findAllByStation(anyList())).thenReturn(
            Arrays.asList(station3, station4));

        LineStationCreateRequest request = new LineStationCreateRequest(station3.getId(),
            station4.getId(), 10, 10);
        lineService.addLineStation(line.getId(), request);

        assertThat(line.getStations()).hasSize(4);

        List<Long> stationIds = line.getStationIds();
        assertThat(stationIds).containsExactlyInAnyOrder(1L, 2L, 3L, 4L);
    }

    @Test
    void removeLineStationAtTheFirstOfLine() {
        when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));
        lineService.removeLineStation(line.getId(), 1L);

        assertThat(line.getStations()).hasSize(2);

        List<Long> stationIds = line.getStationIds();
        assertThat(stationIds.get(0)).isEqualTo(2L);
        assertThat(stationIds.get(1)).isEqualTo(3L);
    }

    @Test
    void removeLineStationBetweenTwo() {
        when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));
        lineService.removeLineStation(line.getId(), 2L);

        verify(lineRepository).save(any());
    }

    @Test
    void removeLineStationAtTheEndOfLine() {
        when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));
        lineService.removeLineStation(line.getId(), 3L);

        assertThat(line.getStations()).hasSize(2);

        List<Long> stationIds = line.getStationIds();
        assertThat(stationIds.get(0)).isEqualTo(1L);
        assertThat(stationIds.get(1)).isEqualTo(2L);
    }
}
