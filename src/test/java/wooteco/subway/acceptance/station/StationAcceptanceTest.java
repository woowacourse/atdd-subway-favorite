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
        createByName("/stations", STATION_NAME_KANGNAM, StationResponse.class);
        createByName("/stations", STATION_NAME_YEOKSAM, StationResponse.class);
        createByName("/stations", STATION_NAME_SEOLLEUNG, StationResponse.class);
        // then
        List<StationResponse> stations = getStations();
        assertThat(stations.size()).isEqualTo(3);

        // when
        deleteStation(stations.get(0).getId());
        // then
        List<StationResponse> stationsAfterDelete = getStations();
        assertThat(stationsAfterDelete.size()).isEqualTo(2);
    }
}
