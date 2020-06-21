package wooteco.subway.service.station;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.line.LineStation;
import wooteco.subway.domain.line.LineStationRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;

import javax.transaction.Transactional;

@SpringBootTest
@Sql("/truncate.sql")
public class StationServiceTest {
	@Autowired
	private StationService stationService;

	@Autowired
	private StationRepository stationRepository;
	@Autowired
	private LineRepository lineRepository;
	@Autowired
	private LineStationRepository lineStationRepository;

	@Test
	@Transactional
	public void removeStation() {
        Station station1 = stationRepository.save(Station.of("강남역"));
        Station station2 = stationRepository.save(Station.of("역삼역"));
        Line line = lineRepository.save(Line.of("2호선", LocalTime.of(5, 30), LocalTime.of(22, 30), 10));

		LineStation lineStation1 = lineStationRepository.save(LineStation.of(null, station1, 10, 10));
		LineStation lineStation2 = lineStationRepository.save(LineStation.of(station1, station2, 10, 10));
		line.addLineStation(lineStation1);
		line.addLineStation(lineStation2);

        stationService.deleteStationById(station1.getId());

        Optional<Station> resultStation = stationRepository.findById(station1.getId());
        assertThat(resultStation).isEmpty();

        Line resultLine = lineRepository.findById(line.getId()).orElseThrow(RuntimeException::new);
        assertThat(resultLine.getStations()).hasSize(1);
    }
}
