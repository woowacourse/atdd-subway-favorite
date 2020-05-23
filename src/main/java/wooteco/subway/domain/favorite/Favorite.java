package wooteco.subway.domain.favorite;

import org.springframework.data.annotation.Id;

import wooteco.subway.service.favorite.dto.FavoriteCreateRequest;

public class Favorite {
	@Id
	private Long id;

	private Long sourceStationId;
	private Long targetStationId;

	public Favorite() {
	}

	public Favorite(Long sourceStationId, Long targetStationId) {
		this.sourceStationId = sourceStationId;
		this.targetStationId = targetStationId;
	}

	public Favorite(Long id, Long sourceStationId, Long targetStationId) {
		this.id = id;
		this.sourceStationId = sourceStationId;
		this.targetStationId = targetStationId;
	}

	public static Favorite of(FavoriteCreateRequest favoriteCreateRequest) {
		return new Favorite(favoriteCreateRequest.getSourceStationId(), favoriteCreateRequest.getTargetStationId());
	}

	public Long getId() {
		return id;
	}

	public Long getSourceStationId() {
		return sourceStationId;
	}

	public Long getTargetStationId() {
		return targetStationId;
	}

	@Override
	public String toString() {
		return "Favorite{" +
			"id=" + id +
			", sourceStationId=" + sourceStationId +
			", targetStationId=" + targetStationId +
			'}';
	}
}
