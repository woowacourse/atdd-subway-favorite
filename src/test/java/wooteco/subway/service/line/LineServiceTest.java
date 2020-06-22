package wooteco.subway.service.line;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalTime;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.line.LineStation;
import wooteco.subway.domain.station.Station;
import wooteco.subway.service.line.dto.LineStationCreateRequest;

@SpringBootTest
@Sql("/truncate.sql")
public class LineServiceTest {
    private static final String STATION_NAME1 = "강남역";
    private static final String STATION_NAME2 = "역삼역";
    private static final String STATION_NAME3 = "선릉역";
    private static final String STATION_NAME4 = "삼성역";

    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private LineStationService lineStationService;

    private LineService lineService;

    private Line line;
    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;

    @BeforeEach
    void setUp() {
        lineService = new LineService(lineStationService, lineRepository);

        station1 = new Station(1L, STATION_NAME1);
        station2 = new Station(2L, STATION_NAME2);
        station3 = new Station(3L, STATION_NAME3);
        station4 = new Station(4L, STATION_NAME4);

        line = lineRepository.save(
                new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5));
        line.addLineStation(new LineStation(null, 1L, 10, 10));
        line.addLineStation(new LineStation(1L, 2L, 10, 10));
        line.addLineStation(new LineStation(2L, 3L, 10, 10));
    }

    // Transactional 이거 너무 중요하다
    // 저기서 findById로 찾은 놈이랑 여기서 save한 놈이랑 주소값이 달랐던 문제가 있었다
    // 이거 없으면 영속성 컨텍스트가 내 생각대로 작동하지 못할 수 있다
    // 까먹지 않기 위한 주석
    @Transactional
    @Test
    void addLineStationAtTheFirstOfLine() {
        LineStationCreateRequest request = new LineStationCreateRequest(null, station4.getId(), 10,
                10);
        lineService.addLineStation(line.getId(), request);

        assertThat(line.getStations().getStations()).hasSize(4);

        List<Long> stationIds = line.getStationIds();
        assertThat(stationIds.get(0)).isEqualTo(4L);
        assertThat(stationIds.get(1)).isEqualTo(1L);
        assertThat(stationIds.get(2)).isEqualTo(2L);
        assertThat(stationIds.get(3)).isEqualTo(3L);
    }

    @Transactional
    @Test
    void addLineStationBetweenTwo() {
        LineStationCreateRequest request = new LineStationCreateRequest(station1.getId(), station4.getId(), 10, 10);
        lineService.addLineStation(line.getId(), request);

        assertThat(line.getStations().getStations()).hasSize(4);

        List<Long> stationIds = line.getStationIds();
        assertThat(stationIds.get(0)).isEqualTo(1L);
        assertThat(stationIds.get(1)).isEqualTo(4L);
        assertThat(stationIds.get(2)).isEqualTo(2L);
        assertThat(stationIds.get(3)).isEqualTo(3L);
    }

    @Transactional
    @Test
    void addLineStationAtTheEndOfLine() {
        LineStationCreateRequest request = new LineStationCreateRequest(station3.getId(), station4.getId(), 10, 10);
        lineService.addLineStation(line.getId(), request);

        assertThat(line.getStations().getStations()).hasSize(4);

        List<Long> stationIds = line.getStationIds();
        assertThat(stationIds.get(0)).isEqualTo(1L);
        assertThat(stationIds.get(1)).isEqualTo(2L);
        assertThat(stationIds.get(2)).isEqualTo(3L);
        assertThat(stationIds.get(3)).isEqualTo(4L);
    }

    @Transactional
    @Test
    void removeLineStationAtTheFirstOfLine() {
        lineService.removeLineStation(line.getId(), 1L);

        assertThat(line.getStations().getStations()).hasSize(2);

        List<Long> stationIds = line.getStationIds();
        assertThat(stationIds.get(0)).isEqualTo(2L);
        assertThat(stationIds.get(1)).isEqualTo(3L);
    }

    @Transactional
    @Test
    void removeLineStationBetweenTwo() {
        lineService.removeLineStation(line.getId(), 2L);

        assertThat(line.getStations().getStations()).hasSize(2);

        List<Long> stationIds = line.getStationIds();
        assertThat(stationIds.get(0)).isEqualTo(1L);
        assertThat(stationIds.get(1)).isEqualTo(3L);
    }

    @Transactional
    @Test
    void removeLineStationAtTheEndOfLine() {
        lineService.removeLineStation(line.getId(), 3L);

        assertThat(line.getStations().getStations()).hasSize(2);

        List<Long> stationIds = line.getStationIds();
        assertThat(stationIds.get(0)).isEqualTo(1L);
        assertThat(stationIds.get(1)).isEqualTo(2L);
    }
}
