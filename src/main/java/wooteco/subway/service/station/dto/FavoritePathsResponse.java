package wooteco.subway.service.station.dto;

import java.util.ArrayList;
import java.util.List;
import wooteco.subway.domain.favoritepath.FavoritePath;

public class FavoritePathsResponse {
    private List<FavoritePathRequest> favoritePathRequests;

    FavoritePathsResponse(List<FavoritePath> favoritePaths) {
        List<FavoritePathRequest> favoritePathRequests = new ArrayList<>();
        for (FavoritePath favoritePath : favoritePaths) {
            favoritePathRequests.add(new FavoritePathRequest());
        }
    }

    public List<FavoritePathRequest> getFavoritePathRequests() {
        return favoritePathRequests;
    }
}
