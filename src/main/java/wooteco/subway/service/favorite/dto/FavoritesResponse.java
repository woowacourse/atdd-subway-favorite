package wooteco.subway.service.favorite.dto;

import java.util.List;

public class FavoritesResponse {
    private final List<FavoriteResponse> favoriteResponses;

    public FavoritesResponse(List<FavoriteResponse> favoriteResponses) {
        this.favoriteResponses = favoriteResponses;
    }

    public List<FavoriteResponse> getFavoriteResponses() {
        return favoriteResponses;
    }
}
