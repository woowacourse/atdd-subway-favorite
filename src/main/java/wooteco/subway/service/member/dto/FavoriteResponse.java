package wooteco.subway.service.member.dto;

import java.util.List;
import java.util.stream.Collectors;

import wooteco.subway.service.member.vo.FavoriteInfo;
import wooteco.subway.service.station.dto.StationResponse;

/**
 *    즐겨찾기 응답DTO 클래스입니다.
 *
 *    @author HyungJu An
 */
public class FavoriteResponse {
	private StationResponse sourceStation;
	private StationResponse targetStation;

	private FavoriteResponse() {
	}

	public FavoriteResponse(final StationResponse sourceStation, final StationResponse targetStation) {
		this.sourceStation = sourceStation;
		this.targetStation = targetStation;
	}

	public static FavoriteResponse of(final FavoriteInfo favoriteInfo) {
		return new FavoriteResponse(StationResponse.of(favoriteInfo.getSourceStation()),
			StationResponse.of(favoriteInfo.getTargetStation()));
	}

	public static List<FavoriteResponse> listOf(final List<FavoriteInfo> favoriteInfos) {
		return favoriteInfos.stream()
			.map(FavoriteResponse::of)
			.collect(Collectors.toList());
	}

	public StationResponse getSourceStation() {
		return sourceStation;
	}

	public StationResponse getTargetStation() {
		return targetStation;
	}
}
