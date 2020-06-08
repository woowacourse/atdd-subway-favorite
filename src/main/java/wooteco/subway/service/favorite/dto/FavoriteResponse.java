package wooteco.subway.service.favorite.dto;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.station.StationIdNameMap;

public class FavoriteResponse {
	private String source;
	private String target;

	private FavoriteResponse() {
	}

	public FavoriteResponse(String source, String target) {
		this.source = source;
		this.target = target;
	}

	public static List<FavoriteResponse> listOf(Set<Favorite> favorites,
		StationIdNameMap stationIdNameMap) {
		return favorites.stream()
			.map(favorite -> new FavoriteResponse(
				stationIdNameMap.getNameById(favorite.getSourceId()),
				stationIdNameMap.getNameById(favorite.getTargetId())))
			.collect(Collectors.toList());
	}

	public String getSource() {
		return source;
	}

	public String getTarget() {
		return target;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		FavoriteResponse that = (FavoriteResponse)o;
		return Objects.equals(source, that.source) &&
			Objects.equals(target, that.target);
	}

	@Override
	public int hashCode() {
		return Objects.hash(source, target);
	}

	@Override
	public String toString() {
		return "FavoriteResponse{" +
			"source='" + source + '\'' +
			", target='" + target + '\'' +
			'}';
	}
}
