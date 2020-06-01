package wooteco.subway.acceptance.line;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.acceptance.AcceptanceTest;
import wooteco.subway.service.line.dto.LineDetailResponse;
import wooteco.subway.service.line.dto.LineResponse;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LineAcceptanceTest extends AcceptanceTest {
    @DisplayName("지하철 노선을 관리")
    @Test
    void manageLine() {
        // when : 노선을 추가한다.
        createLine(LINE_NAME_SINBUNDANG);
        createLine(LINE_NAME_BUNDANG);
        createLine(LINE_NAME_2);
        createLine(LINE_NAME_3);
        // then : 노선이 추가되었다.
        List<LineResponse> lines = getLines();
        assertThat(lines.size()).isEqualTo(4);

        // when : 노선을 가져온다.
        LineDetailResponse line = getLine(lines.get(0).getId());

        // then : 저장된 노선 정보를 확인한다.
        assertThat(line.getId()).isNotNull();
        assertThat(line.getName()).isNotNull();
        assertThat(line.getStartTime()).isNotNull();
        assertThat(line.getEndTime()).isNotNull();
        assertThat(line.getIntervalTime()).isNotNull();

        // when : 노선 정보를 수정한다.
        LocalTime startTime = LocalTime.of(8, 00);
        LocalTime endTime = LocalTime.of(22, 00);
        updateLine(line.getId(), startTime, endTime);

        //then : 노선 정보가 수정되었다.
        LineDetailResponse updatedLine = getLine(line.getId());
        assertThat(updatedLine.getStartTime()).isEqualTo(startTime);
        assertThat(updatedLine.getEndTime()).isEqualTo(endTime);

        // when : 노선을 삭제한다.
        deleteLine(line.getId());

        // then : 노선이 삭제되었다.
        List<LineResponse> linesAfterDelete = getLines();
        assertThat(linesAfterDelete.size()).isEqualTo(3);
    }
}
