package wooteco.subway.domain.member;

import java.util.HashSet;
import java.util.Set;

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

    public boolean existsFavorite(Long sourceId, Long destinationId) {
        return favorites.stream()
                .anyMatch(favorite -> favorite.getSourceId().equals(sourceId) && favorite.getDestinationId().equals(destinationId));
    }
}
