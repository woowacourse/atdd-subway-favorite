package wooteco.subway.acceptance.station;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.acceptance.AcceptanceTest;
import wooteco.subway.service.station.dto.StationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StationAcceptanceTest extends AcceptanceTest {
    @DisplayName("지하철역을 관리")
    @Test
    void manageStation() {
        // when : 역이 있다.
        createStation(STATION_NAME_KANGNAM);
        createStation(STATION_NAME_YEOKSAM);
        createStation(STATION_NAME_SEOLLEUNG);
        // then : 역을 조회한다.
        List<StationResponse> stations = getStations();
        assertThat(stations.size()).isEqualTo(3);

        // when : 역을 제거한다.
        deleteStation(stations.get(0).getId());
        // then : 역이 제거되었다.
        List<StationResponse> stationsAfterDelete = getStations();
        assertThat(stationsAfterDelete.size()).isEqualTo(2);
    }
}
