package woowa.bossdog.subway.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import woowa.bossdog.subway.service.station.dto.StationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StationAcceptanceTest extends AcceptanceTest {

    /**
     * 시나리오
     * 1. 지하철 역을 추가한다. [강남역 - 선릉역 - 역삼역]
     * 2. 전체 지하철 역 목록을 조회한다.
     * 3. 특정 지하철 역의 정보를 상세보기 한다.
     * 4. 특정 지하철 역을 삭제한다.
     * 5. 데이터 롤백
     */

    @DisplayName("지하철 역 관리")
    @Test
    public void manageStation() {

        // 1. 지하철 역 추가
        createStation("강남역");
        createStation("선릉역");
        createStation("역삼역");

        // 2. 지하철 역 목록 조회
        List<StationResponse> responses = listStations();
        assertThat(responses).hasSize(3);
        assertThat(responses.get(0).getName()).isEqualTo("강남역");
        assertThat(responses.get(1).getName()).isEqualTo("선릉역");
        assertThat(responses.get(2).getName()).isEqualTo("역삼역");

        // 3. 지하철 역 단건 조회
        final StationResponse findStation = findStation(responses.get(1).getId());
        assertThat(findStation.getId()).isEqualTo(responses.get(1).getId());
        assertThat(findStation.getName()).isEqualTo(responses.get(1).getName());

        // 4. 특정 지하철 역 삭제
        removeStation(responses.get(1).getId());
        responses = listStations();
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getName()).isEqualTo("강남역");
        assertThat(responses.get(1).getName()).isEqualTo("역삼역");

        // 5. 데이터 롤백
        removeStation(responses.get(0).getId());
        removeStation(responses.get(1).getId());
    }

}
