package woowa.bossdog.subway.service.path;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import woowa.bossdog.subway.domain.Line;
import woowa.bossdog.subway.domain.Station;
import woowa.bossdog.subway.repository.LineRepository;
import woowa.bossdog.subway.repository.StationRepository;
import woowa.bossdog.subway.service.path.dto.PathRequest;
import woowa.bossdog.subway.service.path.dto.PathResponse;
import woowa.bossdog.subway.service.path.dto.PathType;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class PathServiceTest {

    private PathService pathService;

    @Mock private StationRepository stationRepository;
    @Mock private LineRepository lineRepository;
    @Mock private GraphService graphService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        pathService = new PathService(lineRepository, stationRepository, graphService);
    }

    @DisplayName("최단 경로 조회")
    @Test
    void findPathByDistance() {
        // given
        final PathRequest request = new PathRequest("강남역", "서울역", PathType.DISTANCE);
        final List<Line> lines = Lists.newArrayList(
                new Line(1L, "2호선", LocalTime.of(5,30), LocalTime.of(23,30), 10),
                new Line(2L, "4호선", LocalTime.of(5,30), LocalTime.of(23,30), 10));
        final List<Long> path = Lists.newArrayList(1L, 2L, 9L, 8L);
        final List<Station> stations = Lists.newArrayList(
                new Station(1L, "강남역"), new Station(2L, "선릉역"),
                new Station(9L, "사당역"), new Station(8L, "서울역"));

        given(stationRepository.findByName(eq("강남역"))).willReturn(Optional.of(new Station(1L, "강남역")));
        given(stationRepository.findByName(eq("서울역"))).willReturn(Optional.of(new Station(8L, "서울역")));
        given(lineRepository.findAll()).willReturn(lines);
        given(graphService.findPath(any(), any(), any(), any())).willReturn(path);
        given(stationRepository.findAllById(any())).willReturn(stations);

        // when
        final PathResponse response = pathService.findPath(request);

        // then
        assertThat(response.getStations().get(0).getName()).isEqualTo("강남역");
        assertThat(response.getStations().get(1).getName()).isEqualTo("선릉역");
        assertThat(response.getStations().get(2).getName()).isEqualTo("사당역");
        assertThat(response.getStations().get(3).getName()).isEqualTo("서울역");
    }
}