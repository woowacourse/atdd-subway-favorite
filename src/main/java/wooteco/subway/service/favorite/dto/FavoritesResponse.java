package wooteco.subway.service.favorite.dto;

import java.util.List;

public class FavoritesResponse {
    private List<FavoriteResponse> favoriteResponses;

    public FavoritesResponse() {
    }

    public FavoritesResponse(List<FavoriteResponse> favoriteResponses) {
        this.favoriteResponses = favoriteResponses;
    }

    public List<FavoriteResponse> getFavoriteResponses() {
        return favoriteResponses;
    }
}
