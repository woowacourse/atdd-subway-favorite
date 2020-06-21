package wooteco.subway.domain.line;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class LineRepositoryTest {
	@Autowired
	private LineRepository lineRepository;

	@Autowired
	private StationRepository stationRepository;

	@Test
	void addLineStation() {
		// given
		Line line = Line.of("2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
		Line persistLine = lineRepository.save(line);
		Station station1 = stationRepository.save(Station.of("의정부역"));
		Station station2 = stationRepository.save(Station.of("회룡역"));


		persistLine.addLineStation(LineStation.of(null, station1, 10, 10));
		persistLine.addLineStation(LineStation.of(station1, station2, 10, 10));

		// when
		Line resultLine = lineRepository.save(persistLine);

		// then
		assertThat(resultLine.getStations()).hasSize(2);
	}
}
