package wooteco.subway.service.favorite.dto;

import java.util.List;

public class FavoritesResponse {
    private List<FavoriteResponse> favoriteStations;

    public FavoritesResponse() {
    }

    public FavoritesResponse(List<FavoriteResponse> favoriteStations) {
        this.favoriteStations = favoriteStations;
    }

    public static FavoritesResponse of(List<FavoriteResponse> favoriteStations) {
        return new FavoritesResponse(favoriteStations);
    }

    public List<FavoriteResponse> getFavoriteStations() {
        return favoriteStations;
    }
}
