package wooteco.subway.service.favorite.dto;

import wooteco.subway.domain.favorite.Favorite;

public class FavoriteRequest {
	private Long sourceStationId;
	private Long targetStationId;

	private FavoriteRequest() {
	}

	public FavoriteRequest(Long sourceStationId, Long targetStationId) {
		this.sourceStationId = sourceStationId;
		this.targetStationId = targetStationId;
	}

	static public FavoriteRequest of(Favorite favorite) {
		return new FavoriteRequest(favorite.getSourceStationId(), favorite.getTargetStationId());
	}

	public Long getSourceStationId() {
		return sourceStationId;
	}

	public Long getTargetStationId() {
		return targetStationId;
	}

	public Favorite toEntity(Long id) {
		return new Favorite(id, sourceStationId, targetStationId);
	}
}
