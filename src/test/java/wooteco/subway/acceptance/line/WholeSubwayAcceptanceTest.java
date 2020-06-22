package wooteco.subway.acceptance.line;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.service.line.dto.LineDetailResponse;
import wooteco.subway.service.line.dto.LineResponse;
import wooteco.subway.service.station.dto.StationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class WholeSubwayAcceptanceTest extends AcceptanceTest {
    @DisplayName("지하철 노선도 전체 정보 조회")
    @Test
    public void wholeSubway() {
        LineResponse lineResponse1 = createLine("2호선");
        StationResponse stationResponse1 = createByName("/stations", "강남역", StationResponse.class);
        StationResponse stationResponse2 = createByName("/stations", "역삼역", StationResponse.class);
        StationResponse stationResponse3 = createByName("/stations", "삼성역", StationResponse.class);
        addLineStation(lineResponse1.getId(), null, stationResponse1.getId());
        addLineStation(lineResponse1.getId(), stationResponse1.getId(), stationResponse2.getId());
        addLineStation(lineResponse1.getId(), stationResponse2.getId(), stationResponse3.getId());

        LineResponse lineResponse2 = createLine("신분당선");
        StationResponse stationResponse5 = createByName("/stations", "양재역", StationResponse.class);
        StationResponse stationResponse6 = createByName("/stations", "양재시민의숲역", StationResponse.class);
        addLineStation(lineResponse2.getId(), null, stationResponse1.getId());
        addLineStation(lineResponse2.getId(), stationResponse1.getId(), stationResponse5.getId());
        addLineStation(lineResponse2.getId(), stationResponse5.getId(), stationResponse6.getId());

        List<LineDetailResponse> response = retrieveWholeSubway().getLineDetailResponse();
        assertThat(response.size()).isEqualTo(2);
        assertThat(response.get(0).getStations().size()).isEqualTo(3);
        assertThat(response.get(1).getStations().size()).isEqualTo(3);
    }
}
