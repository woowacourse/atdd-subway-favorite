package wooteco.subway.service.member.favorite.dto;

import wooteco.subway.domain.member.FavoriteDetail;

import java.util.List;

public class FavoritesResponse {
	private List<FavoriteDetail> favorites;

	private FavoritesResponse() {
	}

	public FavoritesResponse(List<FavoriteDetail> favorites) {
		this.favorites = favorites;
	}

	public List<FavoriteDetail> getFavorites() {
		return favorites;
	}
}
