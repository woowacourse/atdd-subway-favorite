package wooteco.subway.domain.member.favorite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import wooteco.subway.web.dto.ErrorCode;
import wooteco.subway.web.member.exception.MemberException;

@Embeddable
public class Favorites {
    @OneToMany(mappedBy = "member")
    private List<Favorite> favorites = new ArrayList<>();

    protected Favorites() {
    }

    public Favorites(final List<Favorite> favorites) {
        this.favorites = favorites;
    }

    public static Favorites empty() {
        return new Favorites(new ArrayList<>());
    }

    public void add(Favorite favorite) {
        if (favorites.contains(favorite)) {
            throw new MemberException(String.format("source: %s, target: %s, 이미 존재하는 즐겨찾기입니다.",
                favorite.getSourceStation().getName(), favorite.getTargetStation().getName()),
                ErrorCode.FAVORITE_DUPLICATED);
        }
        favorites.add(favorite);
    }

    public void remove(Favorite favorite) {
        if (!favorites.contains(favorite)) {
            throw new MemberException(String.format("source: %s, target: %s, 존재하지 않는 즐겨찾기입니다.",
                favorite.getSourceStation().getName(), favorite.getTargetStation().getName()),
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

    public List<Favorite> getValues() {
        return new ArrayList<>(favorites);
    }
}
