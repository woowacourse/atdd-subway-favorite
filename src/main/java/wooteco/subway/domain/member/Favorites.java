package wooteco.subway.domain.member;

import java.util.HashSet;
import java.util.Set;

public class Favorites {
    private Set<Favorite> favorites;

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

    public void remove(Long favoriteId) {
        Favorite findFavorite = favorites.stream()
                .filter(favorite -> favorite.isSameId(favoriteId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

        favorites.remove(findFavorite);
    }

    public Set<Long> getFavoriteStationIds() {
        Set<Long> stationIds = new HashSet<>();
        favorites.forEach(favorite -> {
            stationIds.add(favorite.getStartStationId());
            stationIds.add(favorite.getEndStationId());
        });
        return stationIds;
    }
}
