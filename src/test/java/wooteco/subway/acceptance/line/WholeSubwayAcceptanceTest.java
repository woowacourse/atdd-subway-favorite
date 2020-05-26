package wooteco.subway.acceptance.line;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.service.line.dto.LineDetailResponse;
import wooteco.subway.service.line.dto.LineResponse;
import wooteco.subway.service.station.dto.StationResponse;
import wooteco.subway.acceptance.AcceptanceTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class WholeSubwayAcceptanceTest extends AcceptanceTest {
    @DisplayName("지하철 노선도 전체 정보 조회")
    @Test
    public void wholeSubway() {
        LineResponse 이호선 = createLine("2호선");
        StationResponse 강남역 = createStation(STATION_NAME_KANGNAM);
        StationResponse 선릉역 = createStation(STATION_NAME_SEOLLEUNG);
        StationResponse 역삼역 = createStation(STATION_NAME_YEOKSAM);
        addLineStation(이호선.getId(), null, 강남역.getId());
        addLineStation(이호선.getId(), 강남역.getId(), 선릉역.getId());
        addLineStation(이호선.getId(), 선릉역.getId(), 역삼역.getId());

        LineResponse 신분당선 = createLine("신분당선");
        StationResponse 양재역 = createStation(STATION_NAME_YANGJAE);
        StationResponse 양재시민숲역 = createStation(STATION_NAME_YANGJAECITIZON);
        addLineStation(신분당선.getId(), null, 강남역.getId());
        addLineStation(신분당선.getId(), 강남역.getId(), 양재역.getId());
        addLineStation(신분당선.getId(), 양재역.getId(), 양재시민숲역.getId());

        List<LineDetailResponse> response = retrieveWholeSubway().getLineDetailResponse();
        assertThat(response.size()).isEqualTo(2);
        assertThat(response.get(0).getStations().size()).isEqualTo(3);
        assertThat(response.get(1).getStations().size()).isEqualTo(3);
    }
}
