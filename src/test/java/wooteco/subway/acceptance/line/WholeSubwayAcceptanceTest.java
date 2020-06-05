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
		// given : 노선이 존재한다.
		LineResponse secondLine = createLine("2호선");
		// and : 역이 존재한다.
		StationResponse gangNamYeok = createStation("강남역");
		StationResponse yukSamYeok = createStation("역삼역");
		StationResponse samSungYeok = createStation("삼성역");
		// and : 구간이 존재한다.
		addLineStation(secondLine.getId(), null, gangNamYeok.getId());
		addLineStation(secondLine.getId(), gangNamYeok.getId(), yukSamYeok.getId());
		addLineStation(secondLine.getId(), yukSamYeok.getId(), samSungYeok.getId());

		// and : 노선이 존재한다.
		LineResponse sinBunDangLine = createLine("신분당선");

		// and : 역이 존재한다.
		StationResponse yangJaeYeok = createStation("양재역");
		StationResponse yangJaeSiMinSupYeok = createStation("양재시민의숲역");

		// and : 구간이 존재한다.
		addLineStation(sinBunDangLine.getId(), null, gangNamYeok.getId());
		addLineStation(sinBunDangLine.getId(), gangNamYeok.getId(), yangJaeYeok.getId());
		addLineStation(sinBunDangLine.getId(), yangJaeYeok.getId(), yangJaeSiMinSupYeok.getId());

		// then : 전체 노선을 조회한다.
		List<LineDetailResponse> response = retrieveWholeSubway().getLineDetailResponse();
		assertThat(response.size()).isEqualTo(2);
		assertThat(response.get(0).getStations().size()).isEqualTo(3);
		assertThat(response.get(1).getStations().size()).isEqualTo(3);
	}
}
