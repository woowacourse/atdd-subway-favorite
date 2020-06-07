package wooteco.subway.service.favorite.dto;

import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.station.Station;

import java.util.ArrayList;
import java.util.List;

public class FavoriteResponses {
    private List<FavoriteResponse> favoriteResponses;

    public FavoriteResponses() {
    }

    public FavoriteResponses(List<FavoriteResponse> favoriteResponses) {
        this.favoriteResponses = favoriteResponses;
    }

    public static FavoriteResponses of(List<Favorite> favorites, List<Station> sourceStations, List<Station> targetStations) {
        List<FavoriteResponse> favoriteResponses = new ArrayList<>();
        for (int i = 0; i < favorites.size(); i++) {
            FavoriteResponse favoriteResponse = FavoriteResponse.of(favorites.get(i), sourceStations.get(i).getName(), targetStations.get(i).getName());
            favoriteResponses.add(favoriteResponse);
        }
        return new FavoriteResponses(favoriteResponses);
    }

    public List<FavoriteResponse> getFavoriteResponses() {
        return favoriteResponses;
    }
}
