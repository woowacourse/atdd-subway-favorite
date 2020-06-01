package wooteco.subway.acceptance.line;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.acceptance.AcceptanceTest;
import wooteco.subway.service.line.dto.LineDetailResponse;
import wooteco.subway.service.line.dto.LineResponse;
import wooteco.subway.service.station.dto.StationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class WholeSubwayAcceptanceTest extends AcceptanceTest {
    @DisplayName("지하철 노선도 전체 정보 조회")
    @Test
    public void wholeSubway() {
        createLine("2호선");
        createStation(STATION_NAME_KANGNAM);
        createStation(STATION_NAME_SEOLLEUNG);
        createStation(STATION_NAME_YEOKSAM);

        List<LineResponse> lines = getLines();
        List<StationResponse> stations = getStations();

        addLineStation(lines.get(0).getId(), null, stations.get(0).getId());
        addLineStation(lines.get(0).getId(), stations.get(0).getId(), stations.get(1).getId());
        addLineStation(lines.get(0).getId(), stations.get(1).getId(), stations.get(2).getId());

        createLine("신분당선");
        createStation(STATION_NAME_YANGJAE);
        createStation(STATION_NAME_YANGJAECITIZON);

        lines = getLines();
        stations = getStations();
        addLineStation(lines.get(1).getId(), null, stations.get(0).getId());
        addLineStation(lines.get(1).getId(), stations.get(0).getId(), stations.get(3).getId());
        addLineStation(lines.get(1).getId(), stations.get(3).getId(), stations.get(4).getId());

        List<LineDetailResponse> response = getWholeSubway().getLineDetailResponse();
        assertThat(response.size()).isEqualTo(2);
        assertThat(response.get(0).getStations().size()).isEqualTo(3);
        assertThat(response.get(1).getStations().size()).isEqualTo(3);
    }
}
