package wooteco.subway.service.favorite.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FavoritePathsResponse {
    private List<FavoritePathResponse> favoritePaths;

    public FavoritePathsResponse(@JsonProperty("favoritePaths") List<FavoritePathResponse> favoritePaths) {
        this.favoritePaths = favoritePaths;
    }

    public List<FavoritePathResponse> getFavoritePaths() {
        return favoritePaths;
    }
}
