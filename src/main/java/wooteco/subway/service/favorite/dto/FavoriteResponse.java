package wooteco.subway.service.favorite.dto;

import java.util.Objects;
import wooteco.subway.domain.station.Station;
import wooteco.subway.service.station.dto.StationResponse;

public class FavoriteResponse {

	private StationResponse sourceStation;
	private StationResponse targetStation;

	private FavoriteResponse() {
	}

	public FavoriteResponse(StationResponse sourceStation, StationResponse targetStation) {
		this.sourceStation = sourceStation;
		this.targetStation = targetStation;
	}

	public static FavoriteResponse of(Station source, Station target) {
		return new FavoriteResponse(
			StationResponse.of(source),
			StationResponse.of(target));
	}

	public StationResponse getSourceStation() {
		return sourceStation;
	}

	public StationResponse getTargetStation() {
		return targetStation;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		FavoriteResponse that = (FavoriteResponse) o;
		return Objects.equals(sourceStation, that.sourceStation) &&
			Objects.equals(targetStation, that.targetStation);
	}

	@Override
	public int hashCode() {
		return Objects.hash(sourceStation, targetStation);
	}

	@Override
	public String toString() {
		return "FavoriteResponse{" +
			"sourceStation=" + sourceStation +
			", targetStation=" + targetStation +
			'}';
	}
}
