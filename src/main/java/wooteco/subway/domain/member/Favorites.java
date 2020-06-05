package wooteco.subway.domain.member;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class Favorites {
    private Set<Favorite> favorites;

    public Favorites() {
        this.favorites = new LinkedHashSet<>();
    }

    public void add(Favorite favorite) {
        favorites.add(favorite);
    }

    public void deleteFavoriteBy(Long favoriteId) {
        favorites.remove(favorites.stream()
                .filter(favorite -> Objects.equals(favorite.getId(), favoriteId))
                .findAny().orElseThrow(IllegalArgumentException::new));
    }

    public Set<Favorite> getFavorites() {
        return favorites;
    }
}
