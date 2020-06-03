package wooteco.subway.service.favorite.dto;

import javax.validation.constraints.NotNull;
import wooteco.subway.domain.favorite.Favorite;

public class FavoriteRequest {

	@NotNull
	private Long sourceStationId;
	@NotNull
	private Long targetStationId;

	private FavoriteRequest() {
	}

	public FavoriteRequest(Long sourceStationId, Long targetStationId) {
		this.sourceStationId = sourceStationId;
		this.targetStationId = targetStationId;
	}

	public Favorite toFavorite() {
		return new Favorite(sourceStationId, targetStationId);
	}

	public Long getSourceStationId() {
		return sourceStationId;
	}

	public Long getTargetStationId() {
		return targetStationId;
	}
}
