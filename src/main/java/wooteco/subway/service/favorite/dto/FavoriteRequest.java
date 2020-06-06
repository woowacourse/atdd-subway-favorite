package wooteco.subway.service.favorite.dto;

import wooteco.subway.domain.favorite.Favorite;

public class FavoriteRequest {
	private Long sourceStationId;
	private Long targetStationId;

	public FavoriteRequest() {
	}

	public FavoriteRequest(final Long sourceStationId, final Long targetStationId) {
		this.sourceStationId = sourceStationId;
		this.targetStationId = targetStationId;
	}

	static public FavoriteRequest of(Favorite favorite) {
		return new FavoriteRequest(favorite.getSourceStationId(), favorite.getTargetStationId());
	}

	static public Favorite toFavorite(FavoriteRequest favoriteRequest) {
		return new Favorite(favoriteRequest.getSourceStationId(), favoriteRequest.getTargetStationId());
	}

	public Long getSourceStationId() {
		return sourceStationId;
	}

	public Long getTargetStationId() {
		return targetStationId;
	}
}
