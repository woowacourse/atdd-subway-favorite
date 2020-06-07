package wooteco.subway.service.favorite;

import java.util.List;

public class FavoriteResponses {
    private List<FavoriteResponse> favoriteResponses;

    public FavoriteResponses() {
    }

    private FavoriteResponses(List<FavoriteResponse> favoriteResponses) {
        this.favoriteResponses = favoriteResponses;
    }

    public static FavoriteResponses of(List<FavoriteResponse> favoriteResponses) {
        return new FavoriteResponses(favoriteResponses);
    }
    public List<FavoriteResponse> getFavoriteResponses() {
        return favoriteResponses;
    }
}
