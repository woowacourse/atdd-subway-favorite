package woowa.bossdog.subway.service.line;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import woowa.bossdog.subway.domain.Line;
import woowa.bossdog.subway.domain.LineStation;
import woowa.bossdog.subway.domain.Station;
import woowa.bossdog.subway.repository.LineRepository;
import woowa.bossdog.subway.repository.LineStationRepository;
import woowa.bossdog.subway.repository.StationRepository;
import woowa.bossdog.subway.service.line.dto.*;
import woowa.bossdog.subway.service.station.dto.StationResponse;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest
class LineServiceTest {

    private LineService lineService;

    @Mock private LineRepository lineRepository;
    @Mock private StationRepository stationRepository;
    @Mock private LineStationRepository lineStationRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        lineService = new LineService(lineRepository, stationRepository);
    }

    @DisplayName("지하철 노선 추가")
    @Test
    void createLine() {
        // given
        final Line line = new Line("2호선", LocalTime.of(5, 30), LocalTime.of(23, 30), 10);
        given(lineRepository.save(any())).willReturn(line);

        // when
        final LineResponse response = lineService.createLine(new LineRequest("2호선", LocalTime.of(5, 30), LocalTime.of(23, 30), 10));

        // then
        verify(lineRepository).save(any());
        assertThat(response.getId()).isEqualTo(line.getId());
        assertThat(response.getName()).isEqualTo(line.getName());
    }

    @DisplayName("지하철 노선 목록 조회")
    @Test
    void listLine() {
        // given
        final List<Line> lines = new ArrayList<>();
        lines.add(new Line("2호선", LocalTime.of(5, 30), LocalTime.of(23, 30), 10));
        lines.add(new Line("3호선", LocalTime.of(5, 50), LocalTime.of(23, 50), 8));
        given(lineRepository.findAll()).willReturn(lines);

        // when
        final List<LineResponse> responses = lineService.listLines();

        // then
        verify(lineRepository).findAll();
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getName()).isEqualTo("2호선");
        assertThat(responses.get(1).getName()).isEqualTo("3호선");
    }

    @DisplayName("지하철 노선 단건 조회")
    @Test
    void findLine() {
        // given
        final Line line = new Line(1L, "2호선", LocalTime.of(5, 30), LocalTime.of(23, 30), 10);
        given(lineRepository.findById(any())).willReturn(Optional.of(line));

        // when
        final LineResponse response = lineService.findLine(line.getId());

        // then
        verify(lineRepository).findById(eq(1L));
        assertThat(response.getId()).isEqualTo(line.getId());
        assertThat(response.getName()).isEqualTo(line.getName());
        assertThat(response.getStartTime()).isEqualTo(line.getStartTime());
        assertThat(response.getEndTime()).isEqualTo(line.getEndTime());
        assertThat(response.getIntervalTime()).isEqualTo(line.getIntervalTime());
    }

    @DisplayName("지하철 노선 정보 수정")
    @Test
    void updateLine() {
        // given
        Line line = new Line(10L,"2호선", LocalTime.of(5, 30), LocalTime.of(23, 30), 10);
        UpdateLineRequest updateRequest = new UpdateLineRequest("신분당선", LocalTime.of(6, 30), LocalTime.of(22, 30), 15);

        Line updatedLine = new Line("신분당선", LocalTime.of(6, 30), LocalTime.of(22, 30), 15);
        given(lineRepository.findById(any())).willReturn(Optional.of(updatedLine));

        // when
        lineService.updateLine(line.getId(), updateRequest);
        final Line findLine = lineRepository.findById(line.getId())
                .orElseThrow(NoSuchElementException::new);

        // then
        assertThat(findLine.getName()).isEqualTo(updatedLine.getName());
        assertThat(findLine.getStartTime()).isEqualTo(updatedLine.getStartTime());
        assertThat(findLine.getEndTime()).isEqualTo(updatedLine.getEndTime());
        assertThat(findLine.getIntervalTime()).isEqualTo(updatedLine.getIntervalTime());
    }

    @DisplayName("지하철 노선 삭제")
    @Test
    void removeLine() {
        // given
        final Line line = new Line(1L, "2호선", LocalTime.of(5, 30), LocalTime.of(23, 30), 10);

        // when
        lineService.deleteLine(line.getId());

        // then
        verify(lineRepository).deleteById(eq(1L));
    }

    @DisplayName("첫번째 구간 추가")
    @Test
    void addFirstLineStation() {
        // given
        Line line = new Line(63L, "2호선", LocalTime.of(5, 30), LocalTime.of(23, 30), 10);
        Station station = new Station(8L, "강남역");

        given(lineRepository.findById(any())).willReturn(Optional.of(line));
        given(stationRepository.findById(eq(8L))).willReturn(Optional.of(station));

        // when
        LineStationRequest lineStationRequest = new LineStationRequest(null, station.getId(), 10, 10);
        lineService.addLineStation(line.getId(), lineStationRequest);
        final LineDetailResponse findDetailLine = lineService.findLineDetail(line.getId());

        final List<StationResponse> responses = findDetailLine.getStations();

        // then
        assertThat(responses).isNotNull().hasSize(1);
        assertThat(responses.get(0).getId()).isEqualTo(8L);
        assertThat(responses.get(0).getName()).isEqualTo("강남역");
    }

    @DisplayName("구간 삭제")
    @Test
    void deleteLineStation() {
        // given
        Line line = new Line(63L, "2호선", LocalTime.of(5, 30), LocalTime.of(23, 30), 10);
        line.getLineStations().add(new LineStation(line, null, new Station(1L, "사당역"), 10, 10));
        line.getLineStations().add(new LineStation(line, new Station(1L, "사당역"), new Station(2L, "강남역"), 10, 10));
        line.getLineStations().add(new LineStation(line, new Station(2L, "강남역"), new Station(3L, "잠실역"), 10, 10));

        Station station = new Station(2L, "강남역");
        given(lineRepository.findById(any())).willReturn(Optional.of(line));
        given(stationRepository.findById(any())).willReturn(Optional.of(station));

        // when
        lineService.deleteLineStation(line.getId(), station.getId());

        // then
        assertThat(line.getLineStations().get(1).getPreStation().getId()).isEqualTo(1L);
        assertThat(line.getLineStations().get(1).getStation().getId()).isEqualTo(3L);
    }

    @DisplayName("노선과 구간 단건 조회")
    @Test
    void findLineDetail() {
        // given
        final Line line = new Line(63L, "2호선", LocalTime.of(5, 30), LocalTime.of(23, 30), 10);
        final Station 강남역 = new Station(30L, "강남역");
        final Station 역삼역 = new Station(33L, "역삼역");

        line.getLineStations().add(new LineStation(line, null, 강남역, 10, 10));
        line.getLineStations().add(new LineStation(line, 강남역, 역삼역, 10, 10));
        final List<LineStation> lineStations = line.getLineStations();

        given(lineRepository.findById(any())).willReturn(Optional.of(line));
        given(lineStationRepository.findAllByLineId(any())).willReturn(lineStations);
        given(stationRepository.findById(eq(30L))).willReturn(Optional.of(강남역));
        given(stationRepository.findById(eq(33L))).willReturn(Optional.of(역삼역));

        // when
        final LineDetailResponse response = lineService.findLineDetail(line.getId());

        // then
        verify(lineRepository).findById(eq(63L));
        assertThat(response.getStations().get(0).getName()).isEqualTo("강남역");
        assertThat(response.getStations().get(1).getName()).isEqualTo("역삼역");
    }

    @DisplayName("전체 노선과 구간 조회")
    @Test
    void listLineDetail() {
        // given
        final Line line = new Line(63L, "2호선", LocalTime.of(5, 30), LocalTime.of(23, 30), 10);
        final Station 강남역 = new Station(30L, "강남역");
        final Station 역삼역 = new Station(33L, "역삼역");

        line.getLineStations().add(new LineStation(line, null, new Station(30L, "강남역"), 10, 10));
        line.getLineStations().add(new LineStation(line, new Station(30L, "강남역"), new Station(33L, "역삼역"), 10, 10));
        List<Line> lines = new ArrayList<>();
        lines.add(line);

        given(lineRepository.findAll()).willReturn(lines);
        given(lineRepository.findById(eq(63L))).willReturn(Optional.of(line));
        given(stationRepository.findById(eq(30L))).willReturn(Optional.of(강남역));
        given(stationRepository.findById(eq(33L))).willReturn(Optional.of(역삼역));

        // when
        final WholeSubwayResponse response = lineService.listLineDetail();

        // then
        verify(lineRepository).findById(eq(63L));
        assertThat(response.getLineDetailResponse().get(0).getStations().get(0).getName()).isEqualTo("강남역");
        assertThat(response.getLineDetailResponse().get(0).getStations().get(1).getName()).isEqualTo("역삼역");
    }
}