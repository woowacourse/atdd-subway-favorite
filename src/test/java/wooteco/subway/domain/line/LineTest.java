package wooteco.subway.domain.line;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import wooteco.subway.domain.station.Station;

public class LineTest {
	private Line line;

	@BeforeEach
	void setUp() {
        line = Line.of("1호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
		Station station1 = Station.of(1L, "의정부역");
		Station station2 = Station.of(2L, "회룡역");
		Station station3 = Station.of(3L, "망월사역");

		line.addLineStation(LineStation.of(1L, null, station1, 10, 10));
        line.addLineStation(LineStation.of(2L, station1, station2, 10, 10));
        line.addLineStation(LineStation.of(3L, station2, station3, 10, 10));
    }

	@Test
	void addLineStation() {
		Station station4 = Station.of("도봉산역");

        line.addLineStation(LineStation.of(null, station4, 10, 10));
        assertThat(line.getStations()).hasSize(4);
    }

	@Test
	void getLineStations() {
		List<Station> sortedStations = line.getSortedStations();

		assertThat(sortedStations.size()).isEqualTo(3);
		assertThat(sortedStations.get(0).getId()).isEqualTo(1L);
		assertThat(sortedStations.get(1).getId()).isEqualTo(2L);
		assertThat(sortedStations.get(2).getId()).isEqualTo(3L);
	}

	@ParameterizedTest
	@ValueSource(longs = {1L, 2L, 3L})
	void removeLineStation(Long stationId) {
		line.removeLineStationById(stationId);

		assertThat(line.getStations()).hasSize(2);
	}
}
