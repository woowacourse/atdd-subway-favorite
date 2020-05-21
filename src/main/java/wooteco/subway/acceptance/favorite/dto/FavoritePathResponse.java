package wooteco.subway.acceptance.favorite.dto;

import java.util.List;

public class FavoritePathResponse {
    private List<StationPathResponse> favoritePaths;

    public FavoritePathResponse() {
    }

    public FavoritePathResponse(List<StationPathResponse> favoritePaths) {
        this.favoritePaths = favoritePaths;
    }

    public List<StationPathResponse> getFavoritePaths() {
        return favoritePaths;
    }
}
