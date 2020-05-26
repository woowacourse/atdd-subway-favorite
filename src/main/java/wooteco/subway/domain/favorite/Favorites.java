package wooteco.subway.domain.favorite;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Favorites {
    private final Set<Favorite> favorites;

    public Favorites(Set<Favorite> favorites) {
        this.favorites = favorites;
    }

    public Set<Long> findAllIds() {
        Set<Long> ids = new HashSet<>();
        for (Favorite favorite : favorites) {
            ids.add(favorite.getSourceStationId());
            ids.add(favorite.getTargetStationId());
        }
        return Collections.unmodifiableSet(ids);
    }

    public Set<Favorite> getFavorites() {
        return Collections.unmodifiableSet(favorites);
    }

    public boolean hasFavorite(Favorite favorite) {
        return favorites.contains(favorite);
    }
}
