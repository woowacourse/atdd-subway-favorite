package wooteco.subway.service.favorite.dto;

import javax.validation.constraints.NotNull;

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

	public String getSourceStation() {
		return sourceStation;
	}

	public String getTargetStation() {
		return targetStation;
	}
}
