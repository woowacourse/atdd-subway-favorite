package wooteco.subway.service.favorite.dto;

import javax.validation.constraints.NotNull;

public class FavoriteRequest {
	@NotNull
	private Long preStationId;
	@NotNull
	private Long stationId;

	public FavoriteRequest() {
	}

	public FavoriteRequest(Long preStationId, Long stationId) {
		this.preStationId = preStationId;
		this.stationId = stationId;
	}

	public Long getPreStationId() {
		return preStationId;
	}

	public Long getStationId() {
		return stationId;
	}
}
