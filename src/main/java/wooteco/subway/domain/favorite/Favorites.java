package wooteco.subway.domain.favorite;

import static java.util.stream.Collectors.*;

import java.util.List;

public class Favorites {
    private final List<Favorite> favorites;

    public Favorites(List<Favorite> favorites) {
        this.favorites = favorites;
    }

    public List<Long> getDepartureStationIds() {
        return favorites.stream()
            .map(Favorite::getDepartureStationId)
            .collect(toList());
    }

    public List<Long> getArrivalStationIds() {
        return favorites.stream()
            .map(Favorite::getArrivalStationId)
            .collect(toList());
    }
}
