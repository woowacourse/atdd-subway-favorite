package wooteco.subway.domain.favorite;

import org.springframework.data.relational.core.mapping.MappedCollection;
import wooteco.subway.domain.station.Station;
import wooteco.subway.web.member.exception.NotExistFavoriteDataException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Favorites {
    @MappedCollection(keyColumn = "index")
    private List<Favorite> favorites;

    public Favorites() {
        this(new ArrayList<>());
    }

    public Favorites(List<Favorite> favorites) {
        this.favorites = favorites;
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

    public void add(Favorite favorite) {
        favorites.add(favorite);
    }

    public void remove(Favorite favorite) {
        favorites.remove(favorite);
    }

    public Favorite findById(Long id) {
        return favorites.stream()
                .filter(item -> item.isSameId(id))
                .findAny()
                .orElseThrow(() -> new NotExistFavoriteDataException("id = " + id));
    }

    public Favorite findFavoriteBySourceAndTarget(Station source, Station target) {
        return favorites.stream()
                .filter(favorite -> favorite.isSameSourceAndTarget(source, target))
                .findAny()
                .orElseThrow(() -> new NotExistFavoriteDataException("source = " + source + ", target = " + target));
    }

    public int size() {
        return favorites.size();
    }

    public Long getIdByIndex(int index) {
        return favorites.get(index).getId();
    }

    public List<Favorite> getFavorites() {
        return favorites;
    }
}
