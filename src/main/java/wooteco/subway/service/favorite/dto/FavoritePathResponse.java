package wooteco.subway.service.favorite.dto;

import wooteco.subway.service.station.dto.StationResponse;

public class FavoritePathResponse {
	private Long id;
	private StationResponse source;
	private StationResponse target;

	public FavoritePathResponse() {
	}

	public FavoritePathResponse(Long id, StationResponse source, StationResponse target) {
		this.id = id;
		this.source = source;
		this.target = target;
	}

    public Long getId() {
        return id;
    }

    public StationResponse getSource() {
        return source;
    }

    public StationResponse getTarget() {
        return target;
    }
}
