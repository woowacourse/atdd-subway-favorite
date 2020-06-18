package wooteco.subway.service.station;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.line.LineStation;
import wooteco.subway.domain.line.LineStationRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;

@SpringBootTest
public class StationServiceTest {
    @Autowired
    private StationService stationService;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private LineStationRepository lineStationRepository;

    @BeforeEach
    void setUp() {
        stationRepository.deleteAll();
        lineRepository.deleteAll();
    }

    @Test
    @Transactional
    public void removeStation() {
        Station station1 = stationRepository.save(new Station("강남역"));
        Station station2 = stationRepository.save(new Station("역삼역"));
        Line line = lineRepository.save(
            new Line("2호선", LocalTime.of(5, 30), LocalTime.of(22, 30), 10));

        LineStation lineStation1 = new LineStation(null, station1, 10, 10);
        LineStation lineStation2 = new LineStation(station1, station2, 10, 10);

        lineStation1.setLine(line);
        lineStation2.setLine(line);

        lineStationRepository.save(lineStation1);
        lineStationRepository.save(lineStation2);

        stationService.deleteStationById(station1.getId());

        Optional<Station> resultStation = stationRepository.findById(station1.getId());
        Line resultLine = lineRepository.findLineJoinFetch(line.getId());

        assertThat(resultStation).isEmpty();
        assertThat(resultLine.getLineStations().getValues()).hasSize(1);
    }
}
