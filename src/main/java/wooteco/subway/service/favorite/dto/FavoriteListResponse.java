package wooteco.subway.service.favorite.dto;

import wooteco.subway.domain.member.Favorite;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FavoriteListResponse {
    private List<FavoriteResponse> favoriteResponses = new ArrayList<>();

    private FavoriteListResponse() {
    }

    public FavoriteListResponse(
            List<FavoriteResponse> favoriteResponses) {
        this.favoriteResponses = favoriteResponses;
    }

    public static FavoriteListResponse of(Set<Favorite> favorites) {
        List<FavoriteResponse> favoriteResponses = favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
        return new FavoriteListResponse(favoriteResponses);
    }

    public List<FavoriteResponse> getFavoriteResponses() {
        return favoriteResponses;
    }
}
