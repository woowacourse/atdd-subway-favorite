package wooteco.subway.service.favorite.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import wooteco.subway.domain.favorite.Favorite;

public class FavoriteResponse {
	@NotNull
	private String sourceStation;
	@NotNull
	private String targetStation;

	public FavoriteResponse() {
	}

	public FavoriteResponse(final String sourceStation, final String targetStation) {
		this.sourceStation = sourceStation;
		this.targetStation = targetStation;
	}

	public static List<FavoriteResponse> toResponse(List<Favorite> favorites) {
		return favorites.stream()
			.map(x -> new FavoriteResponse(x.getSourceStationId(), x.getTargetStationId()))
	}

	public String getSourceStation() {
		return sourceStation;
	}

	public String getTargetStation() {
		return targetStation;
	}
}
