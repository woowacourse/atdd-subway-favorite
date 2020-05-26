package wooteco.subway.domain.member.favorite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.relational.core.mapping.MappedCollection;

import wooteco.subway.web.dto.ErrorCode;
import wooteco.subway.web.member.exception.MemberException;

public class Favorites {
    @MappedCollection(idColumn = "member", keyColumn = "member_key")
    private List<Favorite> favorites;

    public Favorites(final List<Favorite> favorites) {
        this.favorites = favorites;
    }

    public static Favorites empty() {
        return new Favorites(new ArrayList<>());
    }

    public void add(Favorite favorite) {
        if (favorites.contains(favorite)) {
            throw new MemberException(String.format("source: %d, target: %d, 이미 존재하는 즐겨찾기입니다.",
                favorite.getSourceStationId(), favorite.getTargetStationId()),
                ErrorCode.FAVORITE_DUPLICATED);
        }
        favorites.add(favorite);
    }

    public void remove(Favorite favorite) {
        if (!favorites.contains(favorite)) {
            throw new MemberException(String.format("source: %d, target: %d, 존재하지 않는 즐겨찾기입니다.",
                favorite.getSourceStationId(), favorite.getTargetStationId()),
                ErrorCode.FAVORITE_NOT_FOUND);
        }
        favorites.remove(favorite);
    }

    public Set<Long> getStationIds() {
        return favorites.stream()
            .map(Favorite::getStationIds)
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());

    }

    public List<Favorite> getFavorites() {
        return new ArrayList<>(favorites);
    }
}
