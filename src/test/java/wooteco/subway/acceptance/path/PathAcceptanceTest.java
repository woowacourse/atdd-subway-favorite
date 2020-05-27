package wooteco.subway.acceptance.path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.acceptance.AcceptanceTest;
import wooteco.subway.service.path.dto.PathResponse;
import wooteco.subway.service.station.dto.StationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PathAcceptanceTest extends AcceptanceTest {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        initStation();
    }

    @DisplayName("거리 기준으로 경로 조회")
    @Test
    public void findPathByDistance() {
        PathResponse pathResponse = findPath(STATION_NAME_KANGNAM, STATION_NAME_DOGOK, "DISTANCE");
        List<StationResponse> path = pathResponse.getStations();
        assertThat(path).hasSize(5);
        assertThat(path.get(0).getName()).isEqualTo(STATION_NAME_KANGNAM);
        assertThat(path.get(1).getName()).isEqualTo(STATION_NAME_YEOKSAM);
        assertThat(path.get(2).getName()).isEqualTo(STATION_NAME_SEOLLEUNG);
        assertThat(path.get(3).getName()).isEqualTo(STATION_NAME_HANTI);
        assertThat(path.get(4).getName()).isEqualTo(STATION_NAME_DOGOK);
    }

    @DisplayName("소요시간 기준으로 경로 조회")
    @Test
    public void findPathByDuration() {
        PathResponse pathResponse = findPath(STATION_NAME_KANGNAM, STATION_NAME_DOGOK, "DURATION");
        List<StationResponse> path = pathResponse.getStations();
        assertThat(path).hasSize(4);
        assertThat(path.get(0).getName()).isEqualTo(STATION_NAME_KANGNAM);
        assertThat(path.get(1).getName()).isEqualTo(STATION_NAME_YANGJAE);
        assertThat(path.get(2).getName()).isEqualTo(STATION_NAME_MAEBONG);
        assertThat(path.get(3).getName()).isEqualTo(STATION_NAME_DOGOK);
    }
}
