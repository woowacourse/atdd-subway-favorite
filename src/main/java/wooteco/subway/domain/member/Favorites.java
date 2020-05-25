package wooteco.subway.domain.member;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Favorites {
    private final Set<Favorite> favorites;

    public Favorites(final Set<Favorite> favorites) {
        this.favorites = favorites;
    }

    public void addFavorite(final Favorite favorite) {
        favorites.add(favorite);
    }

    public void removeFavorite(final Long id) {
        favorites.stream()
            .filter(favorite -> favorite.isId(id))
            .findFirst()
            .ifPresent(favorites::remove);
    }

    public List<Long> getAllStations() {
        Stream<Long> sources = getStationIds(Favorite::getSourceStationId);
        Stream<Long> targets = getStationIds(Favorite::getTargetStationId);
        return Stream.concat(sources, targets)
            .collect(Collectors.toList());
    }

    public Stream<Long> getStationIds(Function<Favorite, Long> criteria) {
        return favorites
            .stream()
            .map(criteria);
    }

    public Set<Favorite> getFavorites() {
        return favorites;
    }
}
