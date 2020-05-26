package wooteco.subway.domain.member;

import wooteco.subway.domain.station.Station;
import wooteco.subway.service.member.dto.FavoriteResponse;

import java.util.HashSet;
import java.util.List;
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

    public void add(Long sourceId, Long destinationId) {
        favorites.add(new Favorite(sourceId, destinationId));
    }

    public void removeById(Long sourceId, Long destinationId) {
        favorites.remove(new Favorite(sourceId, destinationId));
    }

    public List<FavoriteResponse> toFavoriteResponses(List<Station> stations) {
        return favorites.stream()
                .map(favorite -> {
                    String sourceName = stations.stream()
                            .filter(station -> station.getId().equals(favorite.getSourceId()))
                            .map(Station::getName)
                            .findFirst()
                            .orElseThrow(RuntimeException::new);
                    String destinationName = stations.stream()
                            .filter(station -> station.getId().equals(favorite.getDestinationId()))
                            .map(Station::getName)
                            .findFirst()
                            .orElseThrow(RuntimeException::new);
                    return new FavoriteResponse(sourceName, destinationName);
                })
                .collect(Collectors.toList());
    }
    //// TODO: 2020/05/25 생성 중복, 없는 것 삭제할 시 중복 처리?
}
