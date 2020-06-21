package wooteco.subway.service.line;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.line.LineStation;
import wooteco.subway.domain.station.Station;
import wooteco.subway.service.line.dto.LineDetailResponse;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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

	private LineStationService lineStationService;

	private Line line;
	private Station station1;
	private Station station2;
	private Station station3;
	private Station station4;

	@BeforeEach
	void setUp() {
		lineStationService = new LineStationService(lineRepository);

		station1 = Station.of(1L, STATION_NAME1);
		station2 = Station.of(2L, STATION_NAME2);
		station3 = Station.of(3L, STATION_NAME3);
		station4 = Station.of(4L, STATION_NAME4);

		line = Line.of(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
		line.addLineStation(LineStation.of(null, station1, 10, 10));
		line.addLineStation(LineStation.of(station1, station2, 10, 10));
		line.addLineStation(LineStation.of(station2, station3, 10, 10));
	}

	@Test
	void findLineWithStationsById() {
		List<Station> stations = Lists.newArrayList(station1, station2, station3);
		when(lineRepository.findById(anyLong())).thenReturn(Optional.of(line));

		LineDetailResponse lineDetailResponse = lineStationService.findLineWithStationsById(1L);

		assertThat(lineDetailResponse.getStations()).hasSize(3);
	}

	@Test
	void wholeLines() {
		Line newLine = Line.of(2L, "신분당선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);

		Station station4 = Station.of(4L, "양재역");
		Station station5 = Station.of(5L, "양재시민의숲역");
		Station station6 = Station.of(6L, "청계산입구역");

		newLine.addLineStation(LineStation.of(null, station4, 10, 10));
		newLine.addLineStation(LineStation.of(station4, station5, 10, 10));
		newLine.addLineStation(LineStation.of(station5, station6, 10, 10));

		List<Station> stations = Lists.newArrayList();

		when(lineRepository.findAll()).thenReturn(Arrays.asList(this.line, newLine));

		List<LineDetailResponse> lineDetails = lineStationService.findLinesWithStations().getLineDetailResponse();

		assertThat(lineDetails).isNotNull();
		assertThat(lineDetails.get(0).getStations().size()).isEqualTo(3);
		assertThat(lineDetails.get(1).getStations().size()).isEqualTo(3);
	}
}
