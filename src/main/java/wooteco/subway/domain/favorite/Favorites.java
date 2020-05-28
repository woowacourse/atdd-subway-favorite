package wooteco.subway.domain.favorite;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import wooteco.subway.exception.EntityNotFoundException;

public class Favorites {
    private final Set<Favorite> favorites;

    public Favorites(Set<Favorite> favorites) {
        this.favorites = new LinkedHashSet<>(Objects.requireNonNull(favorites));
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

    public void addFavorite(Favorite favorite) {
        favorites.add(favorite);
    }

    public void removeFavorite(Favorite favorite) {
        if (!favorites.contains(favorite)) {
            throw new EntityNotFoundException("삭제할 즐겨찾기 경로가 존재하지 않습니다.");
        }
        favorites.remove(favorite);
    }
}
