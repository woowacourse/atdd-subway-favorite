package wooteco.subway.service.member.dto;

import wooteco.subway.domain.member.Favorite;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FavoriteResponse {
	private Long favoriteId;
	private String departStationName;
	private String arriveStationName;

	public FavoriteResponse(Long favoriteId, String departStationName, String arriveStationName) {
		this.favoriteId = favoriteId;
		this.departStationName = departStationName;
		this.arriveStationName = arriveStationName;
	}

	public static List<FavoriteResponse> listOf(List<Favorite> favorites, Map<Long, String> stationMap) {
		return favorites.stream()
				.map(favorite -> new FavoriteResponse(favorite.getId(),
						stationMap.get(favorite.getDepartStationId()), stationMap.get(favorite.getArriveStationId())))
				.collect(Collectors.toList());
	}

	public Long getFavoriteId() {
		return favoriteId;
	}

	public String getDepartStationName() {
		return departStationName;
	}

	public String getArriveStationName() {
		return arriveStationName;
	}
}
