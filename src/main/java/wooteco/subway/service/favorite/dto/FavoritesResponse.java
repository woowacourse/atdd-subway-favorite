package wooteco.subway.service.favorite.dto;

import java.util.List;

import wooteco.subway.domain.favorite.FavoriteStation;

public class FavoritesResponse {
    private List<FavoriteStation> favoriteStations;

    public FavoritesResponse() {
    }

    public FavoritesResponse(List<FavoriteStation> favoriteStations) {
        this.favoriteStations = favoriteStations;
    }

    public static FavoritesResponse of(List<FavoriteStation> favoriteStations) {
        return new FavoritesResponse(favoriteStations);
    }

    public List<FavoriteStation> getFavoriteStations() {
        return favoriteStations;
    }
}
