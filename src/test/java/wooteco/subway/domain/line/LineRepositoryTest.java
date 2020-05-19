package wooteco.subway.domain.line;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;

@DataJdbcTest
public class LineRepositoryTest {
	@Autowired
	private LineRepository lineRepository;

	@Test
	void addLineStation() {
		// given
		Line line = new Line("2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
		Line persistLine = lineRepository.save(line);
		persistLine.addLineStation(new LineStation(null, 1L, 10, 10));
		persistLine.addLineStation(new LineStation(1L, 2L, 10, 10));

		// when
		Line resultLine = lineRepository.save(persistLine);

		// then
		assertThat(resultLine.getStations()).hasSize(2);
	}
}
