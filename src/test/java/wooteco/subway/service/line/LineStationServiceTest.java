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
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.line.dto.LineDetailResponse;

@SpringBootTest
@Sql("/truncate.sql")
public class LineStationServiceTest {
    private static final String STATION_NAME1 = "강남역";
    private static final String STATION_NAME2 = "역삼역";
    private static final String STATION_NAME3 = "선릉역";

    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private StationRepository stationRepository;

    private LineStationService lineStationService;

    private Line line;
    private Station station1;
    private Station station2;
    private Station station3;

    @BeforeEach
    void setUp() {
        lineStationService = new LineStationService(lineRepository, stationRepository);

        station1 = stationRepository.save(new Station(1L, STATION_NAME1));
        station2 = stationRepository.save(new Station(2L, STATION_NAME2));
        station3 = stationRepository.save(new Station(3L, STATION_NAME3));

        line = lineRepository.save(
                new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5));
        line.addLineStation(new LineStation(null, 1L, 10, 10));
        line.addLineStation(new LineStation(1L, 2L, 10, 10));
        line.addLineStation(new LineStation(2L, 3L, 10, 10));
    }

    @Transactional
    @Test
    void findLineWithStationsById() {
        LineDetailResponse lineDetailResponse = lineStationService.findLineWithStationsById(1L);

        assertThat(lineDetailResponse.getStations()).hasSize(3);
    }

    @Transactional
    @Test
    void wholeLines() {
        Line newLine = lineRepository.save(
                new Line(2L, "신분당선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5));
        newLine.addLineStation(new LineStation(null, 4L, 10, 10));
        newLine.addLineStation(new LineStation(4L, 5L, 10, 10));
        newLine.addLineStation(new LineStation(5L, 6L, 10, 10));

        stationRepository.save(new Station(4L, "양재역"));
        stationRepository.save(new Station(5L, "양재시민의숲역"));
        stationRepository.save(new Station(6L, "청계산입구역"));

        List<LineDetailResponse> lineDetails = lineStationService.findLinesWithStations()
                .getLineDetailResponse();

        assertThat(lineDetails).isNotNull();
        assertThat(lineDetails.get(0).getStations().size()).isEqualTo(3);
        assertThat(lineDetails.get(1).getStations().size()).isEqualTo(3);
    }
}
