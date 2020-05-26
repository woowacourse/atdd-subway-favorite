package wooteco.subway.service.member.favorite.dto;

import wooteco.subway.domain.member.Favorites;

import java.util.Set;
import java.util.stream.Collectors;

public class FavoritesResponse {
	private Set<FavoriteResponse> favorites;

	public FavoritesResponse(Set<FavoriteResponse> favorites) {
		this.favorites = favorites;
	}

	public static FavoritesResponse of(Favorites favorites, Long memberId) {
		Set<FavoriteResponse> favoriteResponses = favorites.getFavorites()
				.stream()
				.map(favorite -> FavoriteResponse.of(memberId, favorite))
				.collect(Collectors.toSet());

		return new FavoritesResponse(favoriteResponses);
	}

	public Set<FavoriteResponse> getFavoritesResponse() {
		return favorites;
	}
}
