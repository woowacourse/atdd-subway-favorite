package woowa.bossdog.subway.service.line.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WholeSubwayResponse {

    private List<LineDetailResponse> lineDetailResponse;

    public WholeSubwayResponse(final List<LineDetailResponse> lineDetailResponses) {
        this.lineDetailResponse = lineDetailResponses;
    }

}
