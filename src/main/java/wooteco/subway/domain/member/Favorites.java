package wooteco.subway.domain.member;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.relational.core.mapping.MappedCollection;

public class Favorites {
    @MappedCollection(keyColumn = "favorite_key")
    private List<Favorite> favorites;

    private Favorites() {
    }

    public Favorites(List<Favorite> favorites) {
        this.favorites = favorites;
    }

    public static Favorites empty() {
        return new Favorites(new ArrayList<>());
    }

    public void addFavorite(Favorite favorite) {
        if (favorites.contains(favorite)) {
            throw new IllegalArgumentException("중복되는 즐겨찾기 항목입니다.");
        }
        favorites.add(favorite);
    }

    public List<Long> getStationIds() {
        return favorites.stream()
                .flatMap(Favorite::getStationIdsStream)
                .collect(Collectors.toList());
    }

    public int size() {
        return favorites.size();
    }

    public List<Favorite> getFavorites() {
        return favorites;
    }

    public void delete(Favorite favorite) {
        if (!favorites.contains(favorite)) {
            throw new IllegalArgumentException("존재하지 않는 즐겨찾기 항목입니다.");
        }
        favorites.remove(favorite);
    }
}
