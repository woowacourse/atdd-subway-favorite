package wooteco.subway.domain.member;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import wooteco.subway.exception.notexist.NoFavoriteExistException;

public class Favorites {
    private Set<Favorite> favorites;

    public Favorites(Set<Favorite> favorites) {
        this.favorites = favorites;
    }

    public static Favorites empty() {
        return new Favorites(new HashSet<>());
    }

    public void add(Favorite favorite) {
        favorites.add(favorite);
    }

    public void remove(Favorite favorite) {
        if (!favorites.remove(favorite)) {
            throw new NoFavoriteExistException();
        }
    }

    public Set<Long> extractStationIds() {
        return favorites.stream()
                .map(favorite -> Arrays.asList(favorite.getSource().getId(),
                        favorite.getTarget().getId()))
                .flatMap(List::stream)
                .collect(Collectors.toSet());
    }

    // public boolean hasFavoriteOf(long sourceId, long targetId) {
    //     return favorites.stream()
    //             .anyMatch(favorite -> favorite.hasSourceId(sourceId) && favorite.hasTargetId(targetId));
    // }

    public Set<Favorite> getFavorites() {
        return favorites;
    }
}
