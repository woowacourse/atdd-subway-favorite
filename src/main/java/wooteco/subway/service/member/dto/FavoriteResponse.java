package wooteco.subway.service.member.dto;

import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.Favorites;
import wooteco.subway.domain.station.Stations;

import java.util.List;
import java.util.stream.Collectors;

public class FavoriteResponse {
    private String sourceName;
    private String destinationName;

    private FavoriteResponse() {
    }

    public FavoriteResponse(String sourceName, String destinationName) {
        this.sourceName = sourceName;
        this.destinationName = destinationName;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public static List<FavoriteResponse> toFavoriteResponses(Stations stations, Favorites favorites) {
        return favorites.getFavorites().stream()
                .map(favorite -> toFavoriteResponse(stations, favorite))
                .collect(Collectors.toList());
    }

    private static FavoriteResponse toFavoriteResponse(Stations stations, Favorite favorite) {
        String sourceName = stations.extractStationById(favorite.getSourceId()).getName();
        String destinationName = stations.extractStationById(favorite.getDestinationId()).getName();
        return new FavoriteResponse(sourceName, destinationName);
    }
}
