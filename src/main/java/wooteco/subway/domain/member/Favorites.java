package wooteco.subway.domain.member;

import java.util.HashSet;
import java.util.Optional;
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

    public Optional<Favorite> findById(Long sourceId, Long destinationId) {
        return favorites.stream()
                .filter(favorite -> favorite.getSourceId().equals(sourceId) && favorite.getDestinationId().equals(destinationId))
                .findFirst();
    }

    public boolean contains(Favorite favorite) {
        return favorites.contains(favorite);
    }
}
