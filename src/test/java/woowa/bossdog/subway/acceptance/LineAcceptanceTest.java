package woowa.bossdog.subway.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import woowa.bossdog.subway.service.line.dto.LineResponse;
import woowa.bossdog.subway.service.line.dto.UpdateLineRequest;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LineAcceptanceTest extends AcceptanceTest {

    /**
     * 시나리오
     * 1. 지하철 노선을 추가한다. [2호선, 3호선, 신분당선]
     * 2. 전체 지하철 노선 목록을 조회한다.
     * 3. 특정 지하철 노선의 정보를 상세 보기 한다.
     * 4. 특정 지하철 노선의 정보를 수정한다.
     * 5. 특정 지하철 노선을 삭제한다.
     * 6. 데이터 롤백
     */

    @DisplayName("지하철 노선 관리")
    @Test
    public void manageLine() {
        // 1. 노선 생성
        createLine("2호선", LocalTime.of(5, 30), LocalTime.of(23, 30), 10);
        createLine("3호선", LocalTime.of(5, 50), LocalTime.of(23, 50), 12);
        createLine("신분당선", LocalTime.of(5, 10), LocalTime.of(22, 30), 15);

        // 2. 노선 목록 조회
        List<LineResponse> responses = listLines();
        assertThat(responses).hasSize(3);
        assertThat(responses.get(0).getName()).isEqualTo("2호선");
        assertThat(responses.get(1).getName()).isEqualTo("3호선");
        assertThat(responses.get(2).getName()).isEqualTo("신분당선");

        // 3. 노선 단건 조회
        LineResponse findLine = findLine(responses.get(1).getId());
        assertThat(findLine.getId()).isEqualTo(responses.get(1).getId());
        assertThat(findLine.getName()).isEqualTo(responses.get(1).getName());

        // 4. 노선 정보 수정
        UpdateLineRequest updateRequest = new UpdateLineRequest("9호선", LocalTime.of(4, 10), LocalTime.of(11, 40), 9);
        updateLine(responses.get(1).getId(), updateRequest);

        LineResponse updatedLine = findLine(responses.get(1).getId());
        assertThat(updatedLine.getName()).isEqualTo(updateRequest.getName());
        assertThat(updatedLine.getStartTime()).isEqualTo(updateRequest.getStartTime());
        assertThat(updatedLine.getEndTime()).isEqualTo(updateRequest.getEndTime());
        assertThat(updatedLine.getIntervalTime()).isEqualTo(updateRequest.getIntervalTime());

        // 5. 특정 노선 삭제
        removeLine(responses.get(1).getId());
        responses = listLines();
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getName()).isEqualTo("2호선");
        assertThat(responses.get(1).getName()).isEqualTo("신분당선");

        // 6. 데이터 롤백(나머지 노선 모두 삭제)
        removeLine(responses.get(0).getId());
        removeLine(responses.get(1).getId());
    }
}
