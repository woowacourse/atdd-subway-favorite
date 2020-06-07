package wooteco.subway.domain.favorite;

import java.util.HashSet;
import java.util.Set;

public class FavoriteStations {
    private final Set<FavoriteStation> favorites;

    public FavoriteStations(Set<FavoriteStation> favorites) {
        this.favorites = favorites;
    }

    public static FavoriteStations createEmpty() {
        return new FavoriteStations(new HashSet<>());
    }

    public Set<FavoriteStation> getFavorites() {
        return favorites;
    }

    public void add(FavoriteStation favoriteStation) {
        favorites.add(favoriteStation);
    }

    public FavoriteStation findByNames(Long source, Long target) {
        return favorites.stream()
                .filter(favoriteStation -> favoriteStation.getSource().equals(source) &&
                        favoriteStation.getTarget().equals(target))
                .findFirst()
                .orElseThrow(AssertionError::new);
    }

    public void deleteFavoriteStation(FavoriteStation favoriteStation) {
        favorites.remove(favoriteStation);
    }

    public boolean contain(FavoriteStation favoriteStation) {
        return favorites.contains(favoriteStation);
    }
}
