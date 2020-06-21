package woowa.bossdog.subway.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import woowa.bossdog.subway.service.line.dto.LineResponse;
import woowa.bossdog.subway.service.path.dto.PathRequest;
import woowa.bossdog.subway.service.path.dto.PathResponse;
import woowa.bossdog.subway.service.path.dto.PathType;
import woowa.bossdog.subway.service.station.dto.StationResponse;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class PathAcceptanceTest extends AcceptanceTest {

    /**
     * 시나리오
     * 1. 지하철 역을 추가한다. [강남역, 선릉역, 역삼역, 삼성역, 잠실역, 구의역, 시청역, 서울역, 사당역]
     * 2. 지하철 노선을 추가한다. [2호선, 4호선]
     * 3. 구간을 추가한다.
     *
     * 강남역(2) - 선릉역(2,4) - 역삼역(2) - 삼성역(2)
     *             |                     |
     *           사당역(4)               잠실역(2)
     *             |                     |
     *           서울역(2,4) - 시청역(2) - 구의역(2)
     *
     * 4. 경로를 조회한다. [source : 강남역, target : 서울역],
     * 5. 데이터 롤백
     */

    @DisplayName("경로를 조회한다.")
    @Test
    void managePath() {
        // 1. 지하철 역을 추가한다.
        createStation("강남역");
        createStation("선릉역");
        createStation("역삼역");
        createStation("삼성역");
        createStation("잠실역");
        createStation("구의역");
        createStation("시청역");
        createStation("서울역");
        createStation("사당역");

        // 2. 지하철 노선을 추가한다.
        createLine("2호선", LocalTime.of(5, 30), LocalTime.of(23, 30), 10);
        createLine("4호선", LocalTime.of(5, 30), LocalTime.of(23, 30), 10);

        // 3. 구간을 추가한다.
        List<LineResponse> lineResponses = listLines();
        Long 이호선 = lineResponses.get(0).getId();
        Long 사호선 = lineResponses.get(1).getId();

        List<StationResponse> stationResponses = listStations();
        Long 강남역 = stationResponses.get(0).getId();
        Long 선릉역 = stationResponses.get(1).getId();
        Long 역삼역 = stationResponses.get(2).getId();
        Long 삼성역 = stationResponses.get(3).getId();
        Long 잠실역 = stationResponses.get(4).getId();
        Long 구의역 = stationResponses.get(5).getId();
        Long 시청역 = stationResponses.get(6).getId();
        Long 서울역 = stationResponses.get(7).getId();
        Long 사당역 = stationResponses.get(8).getId();

        addLineStation(이호선, null, 강남역, 10, 10);
        addLineStation(이호선, 강남역, 선릉역, 10, 10);
        addLineStation(이호선, 선릉역, 역삼역, 10, 10);
        addLineStation(이호선, 역삼역, 삼성역, 10, 10);
        addLineStation(이호선, 삼성역, 잠실역, 10, 10);
        addLineStation(이호선, 잠실역, 구의역, 10, 10);
        addLineStation(이호선, 구의역, 시청역, 10, 10);
        addLineStation(이호선, 시청역, 서울역, 10, 10);
        addLineStation(사호선, null, 선릉역, 10, 10);
        addLineStation(사호선, 선릉역, 사당역, 10, 10);
        addLineStation(사호선, 사당역, 서울역, 10, 10);

        // 4. 경로를 조회한다.
        PathResponse response = findPath("강남역", "서울역", PathType.DISTANCE);
        assertThat(response.getStations().get(0).getName()).isEqualTo("강남역");
        assertThat(response.getStations().get(1).getName()).isEqualTo("선릉역");
        assertThat(response.getStations().get(2).getName()).isEqualTo("사당역");
        assertThat(response.getStations().get(3).getName()).isEqualTo("서울역");

        // 5. 데이터 롤백
        IntStream.range(0, 9)
                .forEach(i -> removeStation(stationResponses.get(i).getId()));
        IntStream.range(0, 2)
                .forEach(i -> removeLine(lineResponses.get(i).getId()));
    }

    private PathResponse findPath(final String source, final String target, final PathType type) {
        PathRequest request = new PathRequest(source, target, type);
        // @formatter:off
        return given().
                        body(request).
                        accept(MediaType.APPLICATION_JSON_VALUE).
                        contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                        get("/paths?source=" + source + "&target=" + target + "&type=" + type).
                then().
                        log().all().
                        statusCode(HttpStatus.OK.value()).
                        extract().as(PathResponse.class);
        // @formatter:on
    }
}
