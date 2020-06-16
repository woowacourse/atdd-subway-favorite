package wooteco.subway.service.favorite.dto;

import wooteco.subway.domain.favorite.Favorites;
import wooteco.subway.domain.station.Stations;

import java.util.LinkedList;
import java.util.List;

public class FavoritesResponse {
    private List<FavoriteResponse> favoriteResponses;

    public FavoritesResponse() {
    }

    public FavoritesResponse(List<FavoriteResponse> favoriteResponses) {
        this.favoriteResponses = favoriteResponses;
    }

    public static FavoritesResponse of(Favorites favorites, Stations sourceStations, Stations targetStations) {
        List<FavoriteResponse> favoriteResponses = new LinkedList<>();
        for (int i = 0; i < favorites.size(); i++) {
            Long favoriteId = favorites.getIdByIndex(i);
            String sourceStationName = sourceStations.getNameByIndex(i);
            String targetStationName = targetStations.getNameByIndex(i);
            FavoriteResponse favoriteResponse = new FavoriteResponse(favoriteId, sourceStationName, targetStationName);
            favoriteResponses.add(favoriteResponse);
        }
        return new FavoritesResponse(favoriteResponses);
    }

    public List<FavoriteResponse> getFavoriteResponses() {
        return favoriteResponses;
    }
}
