package wooteco.subway.service.member.favorite.dto;

import wooteco.subway.domain.member.FavoriteDetail;

import java.util.List;

public class FavoritesResponse {
	private List<FavoriteDetail> favorites;

	public FavoritesResponse(List<FavoriteDetail> favorites) {
		this.favorites = favorites;
	}

	public List<FavoriteDetail> getFavoritesResponse() {
		return favorites;
	}
}
