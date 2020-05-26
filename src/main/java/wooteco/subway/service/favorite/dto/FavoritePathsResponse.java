package wooteco.subway.service.favorite.dto;

import java.util.List;

public class FavoritePathsResponse {
    private List<FavoritePathResponse> favoritePaths;

    public FavoritePathsResponse() {
    }

    public FavoritePathsResponse(List<FavoritePathResponse> favoritePaths) {
        this.favoritePaths = favoritePaths;
    }

    public List<FavoritePathResponse> getFavoritePaths() {
        return favoritePaths;
    }
}
