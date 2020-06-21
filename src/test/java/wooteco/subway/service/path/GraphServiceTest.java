package wooteco.subway.service.path;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineStation;
import wooteco.subway.domain.path.PathType;
import wooteco.subway.domain.station.Station;

public class GraphServiceTest {
	private static final String STATION_NAME1 = "강남역";
	private static final String STATION_NAME2 = "역삼역";
	private static final String STATION_NAME3 = "선릉역";
	private static final String STATION_NAME4 = "양재역";
	private static final String STATION_NAME5 = "양재시민의숲역";
	private static final String STATION_NAME6 = "서울역";

	private GraphService graphService;

	private Station station1;
	private Station station2;
	private Station station3;
	private Station station4;
	private Station station5;
	private Station station6;

	private Line line1;
	private Line line2;

	@BeforeEach
	void setUp() {
        graphService = new GraphService();

        station1 = Station.of(1L, STATION_NAME1);
        station2 = Station.of(2L, STATION_NAME2);
        station3 = Station.of(3L, STATION_NAME3);
        station4 = Station.of(4L, STATION_NAME4);
        station5 = Station.of(5L, STATION_NAME5);
        station6 = Station.of(6L, STATION_NAME6);

        line1 = Line.of("2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line1.addLineStation(LineStation.of(null, station1, 10, 10));
        line1.addLineStation(LineStation.of(station1, station2, 10, 10));
        line1.addLineStation(LineStation.of(station2, station3, 10, 10));

        line2 = Line.of("신분당선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line2.addLineStation(LineStation.of(null, station1, 10, 10));
        line2.addLineStation(LineStation.of(station1, station4, 10, 10));
        line2.addLineStation(LineStation.of(station4, station5, 10, 10));
    }

	@Test
	void findPath() {
		List<Long> stationIds = graphService.findPath(Lists.list(line1, line2), station3.getId(), station5.getId(),
			PathType.DISTANCE);

		assertThat(stationIds).hasSize(5);
		assertThat(stationIds.get(0)).isEqualTo(3L);
		assertThat(stationIds.get(1)).isEqualTo(2L);
		assertThat(stationIds.get(2)).isEqualTo(1L);
		assertThat(stationIds.get(3)).isEqualTo(4L);
		assertThat(stationIds.get(4)).isEqualTo(5L);
	}

	@Test
	void findPathWithNoPath() {
		assertThrows(IllegalArgumentException.class, () ->
			graphService.findPath(Lists.list(line1, line2), station3.getId(), station6.getId(), PathType.DISTANCE)
		);

	}

	@Test
	void findPathWithDisconnected() {
		line2.removeLineStationById(station1.getId());

		assertThrows(IllegalArgumentException.class, () ->
			graphService.findPath(Lists.list(line1, line2), station3.getId(), station6.getId(), PathType.DISTANCE)
		);
	}
}
