package wooteco.subway.domain.favorite;

import java.util.List;
import java.util.stream.Collectors;

public class Favorites {
    private final List<Favorite> favorites;

    public Favorites(List<Favorite> favorites) {
        this.favorites = favorites;
    }

    public List<Long> extractIds() {
        return favorites.stream()
                .map(Favorite::getId)
                .collect(Collectors.toList());
    }

    public List<Long> extractSourceIds() {
        return favorites.stream()
                .map(Favorite::getSourceId)
                .collect(Collectors.toList());
    }

    public List<Long> extractTargetIds() {
        return favorites.stream()
                .map(Favorite::getTargetId)
                .collect(Collectors.toList());
    }

    public int size() {
        return favorites.size();
    }

    public List<Favorite> getFavorites() {
        return favorites;
    }

    public Favorite get(int index) {
        return favorites.get(index);
    }
}
