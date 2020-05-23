package wooteco.subway.service.favorite.dto;

public class FavoriteResponse {
	private String preStationName;
	private String stationName;

	public FavoriteResponse(String preStationName, String stationName) {
		this.preStationName = preStationName;
		this.stationName = stationName;
	}

	public String getPreStationName() {
		return preStationName;
	}

	public String getStationName() {
		return stationName;
	}
}
