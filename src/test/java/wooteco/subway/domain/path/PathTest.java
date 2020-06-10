package wooteco.subway.domain.path;

import org.assertj.core.util.Lists;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineStation;
import wooteco.subway.domain.line.Lines;
import wooteco.subway.domain.station.Station;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PathTest {

	private static final String STATION_NAME1 = "강남역";
	private static final String STATION_NAME2 = "역삼역";
	private static final String STATION_NAME3 = "선릉역";
	private static final String STATION_NAME4 = "양재역";
	private static final String STATION_NAME5 = "양재시민의숲역";
	private static final String STATION_NAME6 = "서울역";

	private Path path;

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
		path = new Path(new WeightedMultigraph(DefaultWeightedEdge.class));

		station1 = new Station(1L, STATION_NAME1);
		station2 = new Station(2L, STATION_NAME2);
		station3 = new Station(3L, STATION_NAME3);
		station4 = new Station(4L, STATION_NAME4);
		station5 = new Station(5L, STATION_NAME5);
		station6 = new Station(6L, STATION_NAME6);

		line1 = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
		line1.addLineStation(new LineStation(null, 1L, 10, 10));
		line1.addLineStation(new LineStation(1L, 2L, 10, 10));
		line1.addLineStation(new LineStation(2L, 3L, 10, 10));

		line2 = new Line(2L, "신분당선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
		line2.addLineStation(new LineStation(null, 1L, 10, 10));
		line2.addLineStation(new LineStation(1L, 4L, 10, 10));
		line2.addLineStation(new LineStation(4L, 5L, 10, 10));
	}

	@DisplayName("거리에 따른 최단 경로를 조회")
	@Test
	void findPath() {
		List<Long> stationIds = path.findShortestPath(Lines.of(Lists.list(line1, line2)), station3.getId(), station5.getId(), PathType.DISTANCE);

		assertThat(stationIds).hasSize(5);
		assertThat(stationIds.get(0)).isEqualTo(3L);
		assertThat(stationIds.get(1)).isEqualTo(2L);
		assertThat(stationIds.get(2)).isEqualTo(1L);
		assertThat(stationIds.get(3)).isEqualTo(4L);
		assertThat(stationIds.get(4)).isEqualTo(5L);
	}

	@DisplayName("경로가 없을시 예외처리")
	@Test
	void findPathWithNoPath() {
		assertThrows(IllegalArgumentException.class, () ->
				path.findShortestPath(Lines.of(Lists.list(line1, line2)), station3.getId(), station6.getId(), PathType.DISTANCE)
		);

	}

	@DisplayName("연결이 안되어있을 경우 예외처리")
	@Test
	void findPathWithDisconnected() {
		line2.removeLineStationById(station1.getId());

		assertThrows(IllegalArgumentException.class, () ->
				path.findShortestPath(Lines.of(Lists.list(line1, line2)), station3.getId(), station6.getId(), PathType.DISTANCE)
		);
	}
}
