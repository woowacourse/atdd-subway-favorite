package wooteco.subway.domain.favorite;

import java.util.Collections;
import java.util.Set;

import wooteco.subway.domain.favorite.exception.DuplicatedFavoriteException;

public class Favorites {
    private Set<Favorite> favorites;

    public Favorites(Set<Favorite> favorites) {
        this.favorites = favorites;
    }

    public void add(Favorite favorite) {
        if (favorites.contains(favorite)) {
            throw new DuplicatedFavoriteException("해당 경로는 이미 추가되어 있습니다.");
        }
        favorites.add(favorite);
    }

    public void delete(Long id) {
        favorites.stream()
            .filter(favorite -> favorite.isSameId(id))
            .findFirst()
            .ifPresent(favorites::remove);
    }

    public Set<Favorite> getFavorites() {
        return Collections.unmodifiableSet(favorites);
    }
}
