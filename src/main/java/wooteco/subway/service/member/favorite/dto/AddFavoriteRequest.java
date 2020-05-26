package wooteco.subway.service.member.favorite.dto;

public class AddFavoriteRequest {
	private Long sourceId;
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
