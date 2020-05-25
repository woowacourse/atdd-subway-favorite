package wooteco.subway.service.favorite.dto;

import wooteco.subway.domain.favorite.Favorite;

public class FavoriteResponse {
    private final String source;
    private final String target;

    public FavoriteResponse(String source, String target) {
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite.getSource(), favorite.getTarget());
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }
}
