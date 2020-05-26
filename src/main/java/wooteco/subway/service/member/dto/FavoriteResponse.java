package wooteco.subway.service.member.dto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import wooteco.subway.domain.member.Favorite;

public class FavoriteResponse {
	private Long departStationId;
	private String departStationName;
	private Long arriveStationId;
	private String arriveStationName;

	public FavoriteResponse(Long departStationId, String departStationName, Long arriveStationId,
		String arriveStationName) {
		this.departStationId = departStationId;
		this.departStationName = departStationName;
		this.arriveStationId = arriveStationId;
		this.arriveStationName = arriveStationName;
	}

	public static List<FavoriteResponse> listOf(List<Favorite> favorites, Map<Long, String> stationMap) {
		return favorites.stream()
			.map(favorite -> new FavoriteResponse(favorite.getDepartStationId(),
				stationMap.get(favorite.getDepartStationId()), favorite.getArriveStationId(),
				stationMap.get(favorite.getArriveStationId())))
			.collect(Collectors.toList());
	}

	public Long getDepartStationId() {
		return departStationId;
	}

	public String getDepartStationName() {
		return departStationName;
	}

	public Long getArriveStationId() {
		return arriveStationId;
	}

	public String getArriveStationName() {
		return arriveStationName;
	}
}
