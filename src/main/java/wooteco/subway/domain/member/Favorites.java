package wooteco.subway.domain.member;

import wooteco.subway.domain.station.Station;
import wooteco.subway.service.exception.WrongStationException;
import wooteco.subway.service.member.dto.FavoriteResponse;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Favorites {

    private final Set<Favorite> favorites;

    public Favorites(Set<Favorite> favorites) {
        this.favorites = favorites;
    }

    public static Favorites empty() {
        return new Favorites(new HashSet<>());
    }

    public Set<Favorite> getFavorites() {
        return favorites;
    }

    public void add(Favorite favorite) {
        favorites.add(favorite);
    }

    public void remove(Favorite favorite) {
        favorites.remove(favorite);
    }

    public List<FavoriteResponse> toFavoriteResponses(List<Station> stations) {
        return favorites.stream()
                .map(favorite -> toFavoriteResponse(stations, favorite))
                .collect(Collectors.toList());
    }

    private FavoriteResponse toFavoriteResponse(List<Station> stations, Favorite favorite) {
        String sourceName = stations.stream()
                .filter(station -> station.getId().equals(favorite.getSourceId()))
                .map(Station::getName)
                .findFirst()
                .orElseThrow(WrongStationException::new);
        String destinationName = stations.stream()
                .filter(station -> station.getId().equals(favorite.getDestinationId()))
                .map(Station::getName)
                .findFirst()
                .orElseThrow(WrongStationException::new);
        return new FavoriteResponse(sourceName, destinationName);
    }

    public Optional<Favorite> findById(Long sourceId, Long destinationId) {
        return favorites.stream()
                .filter(favorite -> favorite.getSourceId().equals(sourceId) && favorite.getDestinationId().equals(destinationId))
                .findFirst();
    }
}
