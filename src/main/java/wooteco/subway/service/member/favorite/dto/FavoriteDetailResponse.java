package wooteco.subway.service.member.favorite.dto;

public class FavoriteDetailResponse {
	private Long memberId;
	private Long sourceId;
	private Long targetId;
	private String sourceName;
	private String targetName;

	private FavoriteDetailResponse() {
	}

	public FavoriteDetailResponse(Long memberId, Long sourceId, Long targetId, String sourceName, String targetName) {
		this.memberId = memberId;
		this.sourceId = sourceId;
		this.targetId = targetId;
		this.sourceName = sourceName;
		this.targetName = targetName;
	}


	public Long getMemberId() {
		return memberId;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public Long getTargetId() {
		return targetId;
	}

	public String getSourceName() {
		return sourceName;
	}

	public String getTargetName() {
		return targetName;
	}
}
