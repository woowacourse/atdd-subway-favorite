package wooteco.subway.service.line;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.linestation.LineStation;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.line.dto.LineDetailResponse;

@ExtendWith(MockitoExtension.class)
public class LineStationServiceTest {
    private static final String STATION_NAME1 = "강남역";
    private static final String STATION_NAME2 = "역삼역";
    private static final String STATION_NAME3 = "선릉역";
    private static final String STATION_NAME4 = "삼성역";
    private static final String STATION_NAME5 = "잠실역";
    private static final String STATION_NAME6 = "판교역";

    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationRepository stationRepository;

    private LineStationService lineStationService;

    private Line line;
    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;
    private Station station5;
    private Station station6;
    private LineStation lineStation1;
    private LineStation lineStation2;
    private LineStation lineStation3;

    @BeforeEach
    void setUp() {
        lineStationService = new LineStationService(lineRepository, stationRepository);

        station1 = new Station(1L, STATION_NAME1);
        station2 = new Station(2L, STATION_NAME2);
        station3 = new Station(3L, STATION_NAME3);
        station4 = new Station(4L, STATION_NAME4);
        station5 = new Station(4L, STATION_NAME5);
        station6 = new Station(4L, STATION_NAME6);

        line = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        lineStation1 = new LineStation(null, station1, 10, 10);
        lineStation2 = new LineStation(station1, station2, 10, 10);
        lineStation3 = new LineStation(station2, station3, 10, 10);

        lineStation1.applyLine(line);
        lineStation2.applyLine(line);
        lineStation3.applyLine(line);
    }

    @Test
    void findLineWithStationsById() {
        when(lineRepository.findById(anyLong())).thenReturn(Optional.of(line));

        List<Station> stations = Lists.newArrayList(station1, station2, station3);
        LineDetailResponse lineDetailResponse = lineStationService.findLineWithStationsById(1L);

        assertThat(lineDetailResponse.getStations()).hasSize(3);
    }

    @Test
    void wholeLines() {
        Line newLine = new Line(2L, "신분당선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        LineStation lineStation1 = new LineStation(null, station4, 10, 10);
        LineStation lineStation2 = new LineStation(station4, station5, 10, 10);
        LineStation lineStation3 = new LineStation(station5, station6, 10, 10);
        lineStation1.applyLine(newLine);
        lineStation2.applyLine(newLine);
        lineStation3.applyLine(newLine);

        List<Station> stations = Lists.newArrayList(station1, station2, station3, station4,
            station5, station6);

        when(lineRepository.findAll()).thenReturn(Arrays.asList(this.line, newLine));

        List<LineDetailResponse> lineDetails = lineStationService.findLinesWithStations()
            .getLineDetailResponse();

        assertThat(lineDetails).isNotNull();
        assertThat(lineDetails.get(0).getStations().size()).isEqualTo(3);
        assertThat(lineDetails.get(1).getStations().size()).isEqualTo(3);
    }
}
