package wooteco.subway.service.favorite.dto;

public class FavoriteResponse {
	private String sourceStation;
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
