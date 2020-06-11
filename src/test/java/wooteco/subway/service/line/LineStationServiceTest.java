package wooteco.subway.service.line;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.service.line.dto.LineDetailResponse;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.line.LineStation;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LineStationServiceTest {
    private static final String STATION_NAME1 = "강남역";
    private static final String STATION_NAME2 = "역삼역";
    private static final String STATION_NAME3 = "선릉역";
    private static final String STATION_NAME4 = "삼성역";

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

    @BeforeEach
    void setUp() {
        lineStationService = new LineStationService(lineRepository, stationRepository);

        station1 = new Station(1L, STATION_NAME1);
        station2 = new Station(2L, STATION_NAME2);
        station3 = new Station(3L, STATION_NAME3);
        station4 = new Station(4L, STATION_NAME4);

        line = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5, "bg-yellow-500");
        line.addLineStation(new LineStation(null, 1L, 10, 10));
        line.addLineStation(new LineStation(1L, 2L, 10, 10));
        line.addLineStation(new LineStation(2L, 3L, 10, 10));
    }

    @DisplayName("라인에 있는 모든 역에 대한 id를 찾는 테스트")
    @Test
    void findLineWithStationsById() {
        List<Station> stations = Lists.newArrayList(station1, station2, station3);
        when(lineRepository.findById(anyLong())).thenReturn(Optional.of(line));
        when(stationRepository.findAllById(anyList())).thenReturn(stations);

        LineDetailResponse lineDetailResponse = lineStationService.findLineWithStationsById(1L);

        assertThat(lineDetailResponse.getStations()).hasSize(3);
    }

    @DisplayName("전체노선을 반환하는 테스트")
    @Test
    void wholeLines() {
        Line newLine = new Line(2L, "신분당선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5, "bg-yellow-500");
        newLine.addLineStation(new LineStation(null, 4L, 10, 10));
        newLine.addLineStation(new LineStation(4L, 5L, 10, 10));
        newLine.addLineStation(new LineStation(5L, 6L, 10, 10));

        List<Station> stations = Lists.newArrayList(new Station(1L, "강남역"), new Station(2L, "역삼역"), new Station(3L, "삼성역"), new Station(4L, "양재역"), new Station(5L, "양재시민의숲역"), new Station(6L, "청계산입구역"));

        when(lineRepository.findAll()).thenReturn(Arrays.asList(this.line, newLine));
        when(stationRepository.findAllById(anyList())).thenReturn(stations);

        List<LineDetailResponse> lineDetails = lineStationService.findLinesWithStations().getLineDetailResponse();

        assertThat(lineDetails).isNotNull();
        assertThat(lineDetails.get(0).getStations().size()).isEqualTo(3);
        assertThat(lineDetails.get(1).getStations().size()).isEqualTo(3);
    }
}
