package wooteco.subway.service.member.favorite.dto;

import javax.validation.constraints.NotNull;

public class AddFavoriteRequest {
	@NotNull(message = "즐겨찾기에 추가할 출발역을 입력해주세요.")
	private Long sourceId;
	@NotNull(message = "즐겨찾기에 추가할 도착역을 입력해주세요.")
	private Long targetId;

	public AddFavoriteRequest(Long sourceId, Long targetId) {
		this.sourceId = sourceId;
		this.targetId = targetId;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public Long getTargetId() {
		return targetId;
	}
}
