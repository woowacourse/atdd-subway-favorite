package wooteco.subway.admin.acceptance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PathAcceptanceTest extends AcceptanceTest {
    private static final String STATION_NAME1 = "강남역";
    private static final String STATION_NAME2 = "역삼역";
    private static final String STATION_NAME3 = "선릉역";
    private static final String STATION_NAME4 = "한티역";
    private static final String STATION_NAME5 = "도곡역";
    private static final String STATION_NAME6 = "매봉역";
    private static final String STATION_NAME7 = "양재역";

    /**
     * 강남 - 역삼 - 선릉
     * |           |
     * |          한티
     * |           |
     * 양재 - 매봉 - 도곡
     */
    @Override
    @BeforeEach
    void setUp() {
        super.setUp();

        // 역 등록
        StationResponse stationResponse1 = createStation(STATION_NAME1);
        StationResponse stationResponse2 = createStation(STATION_NAME2);
        StationResponse stationResponse3 = createStation(STATION_NAME3);
        StationResponse stationResponse4 = createStation(STATION_NAME4);
        StationResponse stationResponse5 = createStation(STATION_NAME5);
        StationResponse stationResponse6 = createStation(STATION_NAME6);
        StationResponse stationResponse7 = createStation(STATION_NAME7);

        // 2호선
        LineResponse lineResponse1 = createLine("2호선");
        addLineStation(lineResponse1.getId(), null, stationResponse1.getName(), 0, 0);
        addLineStation(lineResponse1.getId(), stationResponse1.getName(), stationResponse2.getName(), 5, 10);
        addLineStation(lineResponse1.getId(), stationResponse2.getName(), stationResponse3.getName(), 5, 10);

        // 분당선
        LineResponse lineResponse2 = createLine("분당선");
        addLineStation(lineResponse2.getId(), null, stationResponse3.getName(), 0, 0);
        addLineStation(lineResponse2.getId(), stationResponse3.getName(), stationResponse4.getName(), 5, 10);
        addLineStation(lineResponse2.getId(), stationResponse4.getName(), stationResponse5.getName(), 5, 10);

        // 3호선
        LineResponse lineResponse3 = createLine("3호선");
        addLineStation(lineResponse3.getId(), null, stationResponse5.getName(), 0, 0);
        addLineStation(lineResponse3.getId(), stationResponse5.getName(), stationResponse6.getName(), 5, 10);
        addLineStation(lineResponse3.getId(), stationResponse6.getName(), stationResponse7.getName(), 5, 10);

        // 신분당선
        LineResponse lineResponse4 = createLine("신분당선");
        addLineStation(lineResponse4.getId(), null, stationResponse1.getName(), 0, 0);
        addLineStation(lineResponse4.getId(), stationResponse1.getName(), stationResponse7.getName(), 40, 3);

    }

    @DisplayName("거리 기준으로 경로 조회")
    @Test
    public void findPathByDistance() {
        PathResponse pathResponse = findPath(STATION_NAME1, STATION_NAME5, "DISTANCE");
        List<StationResponse> path = pathResponse.getStations();
        assertThat(path).hasSize(5);
        assertThat(path.get(0).getName()).isEqualTo(STATION_NAME1);
        assertThat(path.get(1).getName()).isEqualTo(STATION_NAME2);
        assertThat(path.get(2).getName()).isEqualTo(STATION_NAME3);
        assertThat(path.get(3).getName()).isEqualTo(STATION_NAME4);
        assertThat(path.get(4).getName()).isEqualTo(STATION_NAME5);
    }

    @DisplayName("소요시간 기준으로 경로 조회")
    @Test
    public void findPathByDuration() {
        PathResponse pathResponse = findPath(STATION_NAME1, STATION_NAME5, "DURATION");
        List<StationResponse> path = pathResponse.getStations();
        assertThat(path).hasSize(4);
        assertThat(path.get(0).getName()).isEqualTo(STATION_NAME1);
        assertThat(path.get(1).getName()).isEqualTo(STATION_NAME7);
        assertThat(path.get(2).getName()).isEqualTo(STATION_NAME6);
        assertThat(path.get(3).getName()).isEqualTo(STATION_NAME5);
    }
}
