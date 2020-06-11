package wooteco.subway.service.line.dto;

import java.util.List;

public class WholeSubwayResponse {

	private List<LineDetailResponse> lineDetailResponse;

	private WholeSubwayResponse() {
	}

	public WholeSubwayResponse(List<LineDetailResponse> lineDetailResponse) {
		this.lineDetailResponse = lineDetailResponse;
	}

	public static WholeSubwayResponse of(List<LineDetailResponse> lineDetailResponses) {
		return new WholeSubwayResponse(lineDetailResponses);
	}

	public List<LineDetailResponse> getLineDetailResponse() {
		return lineDetailResponse;
	}
}
