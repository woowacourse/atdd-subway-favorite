package wooteco.subway.service.favorite.dto;

import wooteco.subway.domain.favorite.Favorites;
import wooteco.subway.domain.station.Station;

import java.util.LinkedList;
import java.util.List;

public class FavoritesResponse {
    private List<FavoriteResponse> favoriteResponses;

    public FavoritesResponse() {
    }

    public FavoritesResponse(List<FavoriteResponse> favoriteResponses) {
        this.favoriteResponses = favoriteResponses;
    }

    public static FavoritesResponse of(Favorites favorites, List<Station> sourceStations, List<Station> targetStations) {
        List<FavoriteResponse> favoriteResponses = new LinkedList<>();
        for (int i = 0; i < favorites.size(); i++) {
            Long favoriteId = favorites.get(i).getId();
            String sourceStationName = sourceStations.get(i).getName();
            String targetStationName = targetStations.get(i).getName();
            FavoriteResponse favoriteResponse = new FavoriteResponse(favoriteId, sourceStationName, targetStationName);
            favoriteResponses.add(favoriteResponse);
        }
        return new FavoritesResponse(favoriteResponses);
    }

    public List<FavoriteResponse> getFavoriteResponses() {
        return favoriteResponses;
    }
}
