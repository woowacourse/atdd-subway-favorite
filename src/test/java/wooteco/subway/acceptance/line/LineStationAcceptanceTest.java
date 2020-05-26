package wooteco.subway.acceptance.line;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.service.line.dto.LineDetailResponse;
import wooteco.subway.service.line.dto.LineResponse;
import wooteco.subway.service.station.dto.StationResponse;
import wooteco.subway.acceptance.AcceptanceTest;

import static org.assertj.core.api.Assertions.assertThat;

public class LineStationAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선에서 지하철역 추가 / 제외")
    @Test
    void manageLineStation() {
        StationResponse 강남역 = createStation(STATION_NAME_KANGNAM);
        StationResponse 역삼역 = createStation(STATION_NAME_YEOKSAM);
        StationResponse 선릉역 = createStation(STATION_NAME_SEOLLEUNG);

        LineResponse 이호선 = createLine("2호선");

        addLineStation(이호선.getId(), null, 강남역.getId());
        addLineStation(이호선.getId(), 강남역.getId(), 역삼역.getId());
        addLineStation(이호선.getId(), 역삼역.getId(), 선릉역.getId());

        LineDetailResponse lineDetailResponse = getLine(이호선.getId());
        assertThat(lineDetailResponse.getStations()).hasSize(3);

        removeLineStation(이호선.getId(), 역삼역.getId());

        LineDetailResponse lineResponseAfterRemoveLineStation = getLine(이호선.getId());
        assertThat(lineResponseAfterRemoveLineStation.getStations().size()).isEqualTo(2);
    }
}
