package wooteco.subway.acceptance.line;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.acceptance.AcceptanceTest;
import wooteco.subway.service.line.dto.LineDetailResponse;
import wooteco.subway.service.line.dto.LineResponse;
import wooteco.subway.service.station.dto.StationResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class LineStationAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선에서 지하철역 추가 / 제거")
    @Test
    void manageLineStation() {
        // given : 역이 존재한다.
        StationResponse stationResponse1 = createStation(STATION_NAME_KANGNAM);
        StationResponse stationResponse2 = createStation(STATION_NAME_YEOKSAM);
        StationResponse stationResponse3 = createStation(STATION_NAME_SEOLLEUNG);
        // and : 노선이 존재한다.
        LineResponse lineResponse = createLine("2호선");

        // when : 구간을 추가한다.
        addLineStation(lineResponse.getId(), null, stationResponse1.getId());
        addLineStation(lineResponse.getId(), stationResponse1.getId(), stationResponse2.getId());
        addLineStation(lineResponse.getId(), stationResponse2.getId(), stationResponse3.getId());

        // then : 구간이 추가되었다.
        LineDetailResponse lineDetailResponse = getLine(lineResponse.getId());
        assertThat(lineDetailResponse.getStations()).hasSize(3);

        // when : 구간을 제거한다.
        removeLineStation(lineResponse.getId(), stationResponse2.getId());

        // then : 구간이 제거되었다.
        LineDetailResponse lineResponseAfterRemoveLineStation = getLine(lineResponse.getId());
        assertThat(lineResponseAfterRemoveLineStation.getStations().size()).isEqualTo(2);
    }
}
