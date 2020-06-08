package wooteco.subway.service.line.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WholeSubwayResponse {
    private List<LineDetailResponse> lineDetailResponse;

    public static WholeSubwayResponse of(List<LineDetailResponse> lineDetailResponses) {
        return new WholeSubwayResponse(lineDetailResponses);
    }

    public WholeSubwayResponse(@JsonProperty("lineDetailResponse") List<LineDetailResponse> lineDetailResponse) {
        this.lineDetailResponse = lineDetailResponse;
    }

    public List<LineDetailResponse> getLineDetailResponse() {
        return lineDetailResponse;
    }
}
