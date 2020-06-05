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
        StationResponse kangNamYeok = createStation(STATION_NAME_KANGNAM);
        StationResponse yeokSamYeok = createStation(STATION_NAME_YEOKSAM);
        StationResponse seolLenungYeok = createStation(STATION_NAME_SEOLLEUNG);
        // and : 노선이 존재한다.
        LineResponse secondLine = createLine("2호선");

        // when : 구간을 추가한다.
        addLineStation(secondLine.getId(), null, kangNamYeok.getId());
        addLineStation(secondLine.getId(), kangNamYeok.getId(), yeokSamYeok.getId());
        addLineStation(secondLine.getId(), yeokSamYeok.getId(), seolLenungYeok.getId());

        // then : 구간이 추가되었다.
        LineDetailResponse lineDetailResponse = getLine(secondLine.getId());
        assertThat(lineDetailResponse.getStations()).hasSize(3);

        // when : 구간을 제거한다.
        removeLineStation(secondLine.getId(), yeokSamYeok.getId());

        // then : 구간이 제거되었다.
        LineDetailResponse lineResponseAfterRemoveLineStation = getLine(secondLine.getId());
        assertThat(lineResponseAfterRemoveLineStation.getStations().size()).isEqualTo(2);
    }
}
