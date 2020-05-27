package wooteco.subway.service.member.dto;

import java.util.ArrayList;
import java.util.List;

import wooteco.subway.domain.station.Station;

/**
 *    즐겨찾기 응답DTO 클래스입니다.
 *
 *    @author HyungJu An
 */
public class FavoriteResponse {
	private final Long sourceStationId;
	private final Long targetStationId;
	private final String sourceStationName;
	private final String targetStationName;

	FavoriteResponse(final Long sourceStationId, final Long targetStationId, final String sourceStationName,
		final String targetStationName) {
		this.sourceStationId = sourceStationId;
		this.targetStationId = targetStationId;
		this.sourceStationName = sourceStationName;
		this.targetStationName = targetStationName;
	}

	public static FavoriteResponse of(final Station sourceStation, final Station targetStation) {
		return new FavoriteResponse(sourceStation.getId(), targetStation.getId(), sourceStation.getName(),
			targetStation.getName());
	}

	public static List<FavoriteResponse> listOf(final List<Station> stations) {
		List<FavoriteResponse> favoriteResponses = new ArrayList<>();

		for (int i = 0; i < stations.size(); i += 2) {
			favoriteResponses.add(FavoriteResponse.of(stations.get(i), stations.get(i + 1)));
		}

		return favoriteResponses;
	}

	public Long getSourceStationId() {
		return sourceStationId;
	}

	public Long getTargetStationId() {
		return targetStationId;
	}

	public String getSourceStationName() {
		return sourceStationName;
	}

	public String getTargetStationName() {
		return targetStationName;
	}
}
