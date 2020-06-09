package wooteco.subway.domain.favorite;

import wooteco.subway.service.favorite.dto.FavoriteResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Favorites {
    private List<Favorite> favorites;

    public Favorites() {
    }

    public Favorites(List<Favorite> favorites) {
        this.favorites = favorites;
    }

    public List<Long> getAllSourceTargetStationIds() {
        List<Long> stationIds = favorites.stream()
                .map(Favorite::getSourceStationId)
                .collect(Collectors.toList());

        List<Long> targetStationIds = favorites.stream()
                .map(Favorite::getTargetStationId)
                .collect(Collectors.toList());

        stationIds.addAll(targetStationIds);
        return stationIds;
    }

    public List<FavoriteResponse> toFavoriteResponses(Map<Long, String> stationNames) {
        return favorites.stream()
                .map(favorite -> favorite.toFavoriteResponse(stationNames))
                .collect(Collectors.toList());
    }
}
