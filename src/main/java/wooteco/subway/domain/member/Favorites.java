package wooteco.subway.domain.member;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Favorites {

    public static final String FAVORITE_NOT_EXIST = "등록되어 있지 않은 즐겨찾기입니다.";
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
        if (!favorites.remove(favorite)) {
            throw new IllegalArgumentException(FAVORITE_NOT_EXIST);
        }
        ;
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
