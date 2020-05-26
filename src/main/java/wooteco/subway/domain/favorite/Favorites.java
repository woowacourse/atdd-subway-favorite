package wooteco.subway.domain.favorite;

import wooteco.subway.service.favorite.dto.FavoriteResponse;

import java.util.List;
import java.util.stream.Collectors;

public class Favorites {
    private List<Favorite> favorites;

    public Favorites(List<Favorite> favorites) {
        this.favorites = favorites;
    }

    public List<FavoriteResponse> toFavoriteResponses() {
        return favorites.stream()
                .map(Favorite::toFavoriteResponse)
                .collect(Collectors.toList());
    }
}
