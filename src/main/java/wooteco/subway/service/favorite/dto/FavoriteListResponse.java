package wooteco.subway.service.favorite.dto;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import wooteco.subway.domain.member.Favorite;

public class FavoriteListResponse {
	private List<FavoriteResponse> favoriteResponses = new ArrayList<>();

	private FavoriteListResponse() {
	}

	public FavoriteListResponse(
		List<FavoriteResponse> favoriteResponses) {
		this.favoriteResponses = favoriteResponses;
	}

	public static FavoriteListResponse of(Set<Favorite> favorites) {
		return favorites.stream()
			.map(FavoriteResponse::of)
			.collect(collectingAndThen(toList(), FavoriteListResponse::new));
	}

	public List<FavoriteResponse> getFavoriteResponses() {
		return favoriteResponses;
	}
}
