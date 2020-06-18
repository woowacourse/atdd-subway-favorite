package wooteco.subway.service.member.dto;

import java.util.List;
import java.util.stream.Collectors;

import wooteco.subway.domain.member.favorite.Favorites;

public class FavoriteResponses {
    private List<FavoriteResponse> favoriteResponses;

    private FavoriteResponses() {
    }

    public FavoriteResponses(final List<FavoriteResponse> favoriteResponses) {
        this.favoriteResponses = favoriteResponses;
    }

    public static FavoriteResponses of(final Favorites favorites) {
        return favorites.getValues().stream()
            .map(FavoriteResponse::new)
            .collect(Collectors.collectingAndThen(Collectors.toList(), FavoriteResponses::new));
    }

    public List<FavoriteResponse> getFavoriteResponses() {
        return favoriteResponses;
    }
}
