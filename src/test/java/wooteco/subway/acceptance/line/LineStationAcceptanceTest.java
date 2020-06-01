package wooteco.subway.acceptance.line;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.acceptance.AcceptanceTest;
import wooteco.subway.service.line.dto.LineDetailResponse;
import wooteco.subway.service.line.dto.LineResponse;
import wooteco.subway.service.station.dto.StationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LineStationAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선에서 지하철역 추가 / 제외")
    @Test
    void manageLineStation() {
        createLine(LINE_NAME_2);
        List<LineResponse> lines = getLines();

        createStation(STATION_NAME_KANGNAM);
        createStation(STATION_NAME_YEOKSAM);
        createStation(STATION_NAME_SEOLLEUNG);
        List<StationResponse> stations = getStations();

        addLineStation(lines.get(0).getId(), null, stations.get(0).getId());
        addLineStation(lines.get(0).getId(), stations.get(0).getId(), stations.get(1).getId());
        addLineStation(lines.get(0).getId(), stations.get(1).getId(), stations.get(2).getId());


        LineDetailResponse lineDetailResponse = getLine(lines.get(0).getId());
        assertThat(lineDetailResponse.getStations()).hasSize(3);

        removeLineStation(lines.get(0).getId(), stations.get(1).getId());
        LineDetailResponse lineResponseAfterRemoveLineStation = getLine(lines.get(0).getId());
        assertThat(lineResponseAfterRemoveLineStation.getStations().size()).isEqualTo(2);
    }
}
