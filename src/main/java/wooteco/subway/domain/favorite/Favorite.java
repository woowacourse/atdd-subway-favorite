package wooteco.subway.domain.favorite;

import java.util.Objects;

import wooteco.subway.service.favorite.dto.FavoriteRequest;

public class Favorite {
	private Long sourceStationId;
	private Long targetStationId;

	public Favorite() {
	}

	public Favorite(Long sourceStationId, Long targetStationId) {
		this.sourceStationId = sourceStationId;
		this.targetStationId = targetStationId;
	}

	public static Favorite of(FavoriteRequest favoriteRequest) {
		return new Favorite(favoriteRequest.getSourceStationId(),
			favoriteRequest.getTargetStationId());
	}

	public Long getSourceStationId() {
		return sourceStationId;
	}

	public Long getTargetStationId() {
		return targetStationId;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		final Favorite favorite = (Favorite)o;
		return Objects.equals(sourceStationId, favorite.sourceStationId) &&
			Objects.equals(targetStationId, favorite.targetStationId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(sourceStationId, targetStationId);
	}
}
