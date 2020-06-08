package wooteco.subway.acceptance.station;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.service.station.dto.StationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StationAcceptanceTest extends AcceptanceTest {
    @DisplayName("지하철역을 관리한다")
    @Test
    void manageStation() {
        // when
        createStation(STATION_NAME_KANGNAM);
        createStation(STATION_NAME_YEOKSAM);
        createStation(STATION_NAME_SEOLLEUNG);
        // then
        List<StationResponse> stations = getList("/stations", StationResponse.class);
        assertThat(stations.size()).isEqualTo(3);

        // when
        delete("/stations/" + stations.get(0).getId());
        // then
        List<StationResponse> stationsAfterDelete = getList("/stations", StationResponse.class);
        assertThat(stationsAfterDelete.size()).isEqualTo(2);
    }
}
