package wooteco.subway.service.member.dto;

import wooteco.subway.domain.member.Favorite;
import wooteco.subway.service.station.dto.StationResponse;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 즐겨찾기 응답DTO 클래스입니다.
 *
 * @author HyungJu An
 */
public class FavoriteResponse {
	private Long id;
	private StationResponse sourceStation;
	private StationResponse targetStation;

	private FavoriteResponse() {
	}

	public FavoriteResponse(final Long id, final StationResponse sourceStation, final StationResponse targetStation) {
		this.id = id;
		this.sourceStation = sourceStation;
		this.targetStation = targetStation;
	}

	public static FavoriteResponse of(final Favorite favorite) {
		return new FavoriteResponse(favorite.getId(), StationResponse.of(favorite.getSourceStation()),
				StationResponse.of(favorite.getTargetStation()));
	}

	public static List<FavoriteResponse> listOf(final Collection<Favorite> favorites) {
		return favorites.stream()
				.map(FavoriteResponse::of)
				.collect(Collectors.toList());
	}

	public Long getId() {
		return id;
	}

	public StationResponse getSourceStation() {
		return sourceStation;
	}

	public StationResponse getTargetStation() {
		return targetStation;
	}
}
